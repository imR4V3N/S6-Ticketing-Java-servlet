// Booking.java
package mg.ticketing.models.flight;

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

//    public void book(Connection connection, String date, Flight flight1, User user, Part picture) throws Exception {
//        String file_name = user.getUsername()+user.getId()+ Utils.getFileExtension(picture);
//
//        Rules rules = new Rules().getRules();
//        Flight flight = flight1.getById(connection);
//        Timestamp bookingDate = Timestamp.valueOf(Utils.format(date));
//        if (picture == null || picture.getSize() <= 0) {
//            throw new Exception("You can't book a flight! You need a passport to book a flight");
//        } else {
//            if (flight.getStartDate().getTime() - bookingDate.getTime() < rules.getHoursBooking()) {
//                throw new Exception("You can't book this flight! You must book at least " + rules.getHoursBooking() + " hours before the flight");
//            } else {
//                int available = flight.getNumberPlacesAvailable(connection, this.getIdPlace());
//                if (available < this.getPlaceNumber()) {
//                    throw new Exception("You can't book this flight! Not enough places available : " + available);
//                } else {
//                    FlightPromotion promotion = new FlightPromotion();
//                    promotion.setIdFlight(flight.getId());
//                    promotion.setIdPlace(this.getIdPlace());
//                    promotion = promotion.getByIdFlightAndIdPlace(connection);
//                    double price = flight.getPriceByIdPlace(connection, this.getIdPlace()).getPrice();
//
//                    if (promotion != null) {
//                        Age age = new Age(this.getIdAge(), null, 0,0);
//                        age = age.getById(connection);
//
//                        int placesToReserve = this.getPlaceNumber();
//
//                        int promoUsed = Math.min(placesToReserve, promotion.getPlaceNumber());
//                        int nonPromoUsed = placesToReserve - promoUsed;
//
//                        double promPercent = promotion.getPromotion() * age.getPromotion();
//
//                        double promoPrice = price * (promPercent / 100);
//
//                        double totalPromoPrice = promoUsed * promoPrice;
//                        double totalNonPromoPrice = nonPromoUsed * price;
//
//                        double totalPrice = totalPromoPrice + totalNonPromoPrice;
//
//                        Booking booking = new Booking(user.getId(), flight.getId());
//                        booking.setIdPlace(this.getIdPlace());
//                        booking.setPlaceNumber(this.getPlaceNumber());
//                        booking.setPrice(totalPrice);
//                        booking.setPicture(file_name);
//                        booking.setIdAge(this.getIdAge());
//                        booking.save(connection);
//
//                        promotion.setPlaceNumber(promotion.getPlaceNumber() - promoUsed);
//                        promotion.update(connection);
//
//                        if (promotion.getPlaceNumber() == 0) {
//                            promotion.delete(connection);
//                        }
//
//                    } else {
//                        Booking booking = new Booking(user.getId(), flight.getId());
//                        booking.setIdPlace(this.getIdPlace());
//                        booking.setPlaceNumber(this.getPlaceNumber());
//                        booking.setPrice(price * this.getPlaceNumber());
//                        booking.setPicture(file_name);
//                        booking.setIdAge(this.getIdAge());
//                        booking.save(connection);
//                    }
//
//                    user.upload(picture, file_name);
//                }
//            }
//        }
//
//    }

//    RESERVATION
    public void book(Connection connection, String date, Flight flightInput, User user, Part picture) throws Exception {
        validatePicture(picture);

        String fileName = buildFileName(user, picture);
        Rules rules = new Rules().getRules();
        Flight flight = flightInput.getById(connection);
        Timestamp bookingDate = Timestamp.valueOf(Utils.format(date));

        validateBookingDate(flight, bookingDate, rules);
        validateSeatAvailability(connection, flight);

        double basePrice = flight.getPriceByIdPlace(connection, this.getIdPlace()).getPrice();
        Booking booking = createBaseBooking(user, flight, fileName);

        FlightPromotion promotion = fetchPromotion(connection, flight);

        double totalPrice = (promotion != null)
                ? applyPromotionAndGetTotal(connection, promotion, basePrice, booking)
                : applyNoPromotionAndGetTotal(basePrice, booking);

        booking.setPrice(totalPrice);
        booking.save(connection);

        user.upload(picture, fileName);
    }

    private void validatePicture(Part picture) throws Exception {
        if (picture == null || picture.getSize() <= 0) {
            throw new Exception("You can't book a flight! You need a passport to book a flight");
        }
    }

    private String buildFileName(User user, Part picture) {
        return user.getUsername() + user.getId() + Utils.getFileExtension(picture);
    }

    private void validateBookingDate(Flight flight, Timestamp bookingDate, Rules rules) throws Exception {
        long timeBeforeFlight = flight.getStartDate().getTime() - bookingDate.getTime();
        if (timeBeforeFlight < rules.getHoursBooking()) {
            throw new Exception("You can't book this flight! You must book at least " + rules.getHoursBooking() + " hours before the flight");
        }
    }

    private void validateSeatAvailability(Connection connection, Flight flight) throws Exception {
        int availableSeats = flight.getNumberPlacesAvailable(connection, this.getIdPlace());
        if (availableSeats < this.getPlaceNumber()) {
            throw new Exception("You can't book this flight! Not enough places available: " + availableSeats);
        }
    }

    private Booking createBaseBooking(User user, Flight flight, String fileName) {
        Booking booking = new Booking(user.getId(), flight.getId());
        booking.setIdPlace(this.getIdPlace());
        booking.setPlaceNumber(this.getPlaceNumber());
        booking.setPicture(fileName);
        booking.setIdAge(this.getIdAge());
        return booking;
    }

    private FlightPromotion fetchPromotion(Connection connection, Flight flight) throws Exception {
        FlightPromotion promotion = new FlightPromotion();
        promotion.setIdFlight(flight.getId());
        promotion.setIdPlace(this.getIdPlace());
        return promotion.getByIdFlightAndIdPlace(connection);
    }

    private double applyPromotionAndGetTotal(Connection connection, FlightPromotion promotion, double price, Booking booking) throws Exception {
        Age age = new Age(this.getIdAge(), null, 0, 0).getById(connection);
        int totalSeats = this.getPlaceNumber();

        int promoUsed = Math.min(totalSeats, promotion.getPlaceNumber());
        int nonPromoUsed = totalSeats - promoUsed;

        double promoRate = promotion.getPromotion() * age.getPromotion();
        double promoPrice = price * (promoRate / 100);
        double totalPrice = (promoUsed * promoPrice) + (nonPromoUsed * price);

        promotion.setPlaceNumber(promotion.getPlaceNumber() - promoUsed);
        promotion.update(connection);

        if (promotion.getPlaceNumber() == 0) {
            promotion.delete(connection);
        }

        return totalPrice;
    }

    private double applyNoPromotionAndGetTotal(double price, Booking booking) {
        return price * booking.getPlaceNumber();
    }



//    ANNULATION
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
