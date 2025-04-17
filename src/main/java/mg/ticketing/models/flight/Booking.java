// Booking.java
package mg.ticketing.models.flight;

import com.google.gson.annotations.Expose;
import jakarta.servlet.http.Part;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.person.Age;
import mg.ticketing.models.person.User;
import mg.ticketing.models.plane.PlaceType;
import mg.ticketing.models.utils.Rules;
import mg.ticketing.models.utils.Utils;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Arrays;

@Table(name = "booking")
public class Booking extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idUser;
    @Column
    private int idFlight;
    @Column
    private int idPlace;
    @Column(name = "place_number")
    private int placeNumber;
    @Column
    private double price;
    @Column
    private int idAge;
    @Column
    private String picture;

    private Age age;
    private User user;
    private Flight flight;
    private PlaceType place;

    public Booking() {}
    public Booking(int idUser, int idFlight) {
        this.idUser = idUser;
        this.idFlight = idFlight;
    }
    public Booking(int id, int idUser, int idFlight) {
        this.id = id;
        this.idUser = idUser;
        this.idFlight = idFlight;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public int getIdFlight() { return idFlight; }
    public void setIdFlight(int idFlight) { this.idFlight = idFlight; }

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PlaceType getPlace() {
        return place;
    }

    public void setPlace(PlaceType place) {
        this.place = place;
    }

    public int getIdAge() {
        return idAge;
    }

    public void setIdAge(int idAge) {
        this.idAge = idAge;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void save(Connection connection) {
        this.create(connection);
    }

    public void delete(Connection connection) {
        this.delete("id", connection);
    }

    public Booking[] getAll(Connection connection) {
        return this.read("", connection).toArray(Booking[]::new);
    }

    public Booking[] init(Connection connection) {
        Booking[] bookings = this.getAll(connection);
        if (bookings.length > 0 && bookings != null) {
            for (Booking booking : bookings) {
                User user1 = new User();
                user1.setId(booking.getIdUser());
                booking.setUser(user1.getById(connection));

                Flight flight1 = new Flight();
                flight1.setId(booking.getIdFlight());
                booking.setFlight(flight1.getById(connection));

                PlaceType placeType = new PlaceType();
                placeType.setId(booking.getIdPlace());
                booking.setPlace(placeType.getById(connection));

                Age age1 = new Age(booking.getIdAge(), null, 0, 0);
                booking.setAge(age1.getById(connection));
            }
        }
        return bookings;
    }

    public Booking getById(Connection connection) {
        Booking[] bookings = this.init(connection);
        return Arrays.stream(bookings).filter(booking -> booking.getId() == this.getId()).findFirst().orElse(null);
    }

    public Booking[] getByIdUserAndIdFlight(Connection connection) {
        Booking[] bookings = this.init(connection);
        return Arrays.stream(bookings).filter(booking -> booking.getIdUser() == this.getIdUser() && booking.getIdFlight() == this.getIdFlight()).toArray(Booking[]::new);
    }

    public Booking[] getByIdFlight(Connection connection) {
        Booking[] bookings = this.init(connection);
        return Arrays.stream(bookings).filter(booking -> booking.getIdFlight() == this.getIdFlight()).toArray(Booking[]::new);
    }

    public void book(Connection connection, String idplace, String number, String date, Flight flight1, User user, int idage, Part picture) throws Exception {
        String file_name = user.getUsername()+user.getId()+ Utils.getFileExtension(picture);

        Rules rules = new Rules().getRules();
        Flight flight = flight1.getById(connection);
        Timestamp bookingDate = Timestamp.valueOf(Utils.format(date));
        if (picture == null || picture.getSize() <= 0) {
            throw new Exception("You can't book a flight! You need a passport to book a flight");
        } else {
            if (flight.getStartDate().getTime() - bookingDate.getTime() < rules.getHoursBooking()) {
                throw new Exception("You can't book this flight! You must book at least " + rules.getHoursBooking() + " hours before the flight");
            } else {
                int available = flight.getNumberPlacesAvailable(connection, Integer.valueOf(idplace));
                if (available < Integer.valueOf(number)) {
                    throw new Exception("You can't book this flight! Not enough places available : " + available);
                } else {
                    FlightPromotion promotion = new FlightPromotion();
                    promotion.setIdFlight(flight.getId());
                    promotion.setIdPlace(Integer.valueOf(idplace));
                    promotion = promotion.getByIdFlightAndIdPlace(connection);
                    double price = flight.getPriceByIdPlace(connection, Integer.valueOf(idplace)).getPrice();

                    if (promotion != null) {
                        Age age = new Age(idage, null, 0,0);
                        age = age.getById(connection);

                        int placesToReserve = Integer.valueOf(number);

                        int promoUsed = Math.min(placesToReserve, promotion.getPlaceNumber());
                        int nonPromoUsed = placesToReserve - promoUsed;

                        double promPercent = promotion.getPromotion() * age.getPromotion();

                        double promoPrice = price * (promPercent / 100);

                        double totalPromoPrice = promoUsed * promoPrice;
                        double totalNonPromoPrice = nonPromoUsed * price;

                        double totalPrice = totalPromoPrice + totalNonPromoPrice;

                        Booking booking = new Booking(user.getId(), flight.getId());
                        booking.setIdPlace(Integer.valueOf(idplace));
                        booking.setPlaceNumber(Integer.valueOf(number));
                        booking.setPrice(totalPrice);
                        booking.setPicture(file_name);
                        booking.setIdAge(idage);
                        booking.save(connection);

                        promotion.setPlaceNumber(promotion.getPlaceNumber() - promoUsed);
                        promotion.update(connection);

                        if (promotion.getPlaceNumber() == 0) {
                            promotion.delete(connection);
                        }

                    } else {
                        Booking booking = new Booking(user.getId(), flight.getId());
                        booking.setIdPlace(Integer.valueOf(idplace));
                        booking.setPlaceNumber(Integer.valueOf(number));
                        booking.setPrice(price * Integer.valueOf(number));
                        booking.setPicture(file_name);
                        booking.setIdAge(idage);
                        booking.save(connection);
                    }

                    user.upload(picture, file_name);
                }
            }
        }

    }

    public void cancelBooking(Connection connection, String id, String date, Flight flight1) {
        Rules rules = new Rules().getRules();
        Flight flight = flight1.getById(connection);
        Timestamp canceledDate = Timestamp.valueOf(Utils.format(date));

        if (flight.getStartDate().getTime() - canceledDate.getTime() < rules.getHoursAnnulation()) {
            throw new RuntimeException("You can't cancel this booking! You must cancel at least " + rules.getHoursBooking() + " hours before the flight");
        } else {
            Booking booking = new Booking();
            booking.setId(Integer.valueOf(id));
            booking.delete(connection);
        }
    }
}
