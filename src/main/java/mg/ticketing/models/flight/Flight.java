// Flight.java
package mg.ticketing.models.flight;

import com.google.gson.annotations.Expose;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.location.City;
import mg.ticketing.models.plane.PlaceType;
import mg.ticketing.models.plane.Plane;
import mg.ticketing.models.plane.PlaneModel;
import mg.ticketing.models.plane.PlanePlace;
import mg.ticketing.models.utils.Utils;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;

@Table(name = "flight")
public class Flight extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idPlane;
    @Column
    private int idStartCity;
    @Column
    private int idDestinationCity;
    @Column(name = "start_date")
    private Timestamp startDate;
    private City startCity;
    private City destinationCity;
    private Plane plane;

    public Flight() {}

    public Flight(int idPlane, int idStartCity, int idDestinationCity, Timestamp startDate) {
        this.idPlane = idPlane;
        this.idStartCity = idStartCity;
        this.idDestinationCity = idDestinationCity;
        this.startDate = startDate;
    }

    public Flight(int id, int idPlane, int idStartCity, int idDestinationCity, Timestamp startDate) {
        this.id = id;
        this.idPlane = idPlane;
        this.idStartCity = idStartCity;
        this.idDestinationCity = idDestinationCity;
        this.startDate = startDate;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPlane() { return idPlane; }
    public void setIdPlane(int idPlane) { this.idPlane = idPlane; }

    public int getIdStartCity() {
        return idStartCity;
    }

    public void setIdStartCity(int idStartCity) {
        this.idStartCity = idStartCity;
    }

    public int getIdDestinationCity() {
        return idDestinationCity;
    }

    public void setIdDestinationCity(int idDestinationCity) {
        this.idDestinationCity = idDestinationCity;
    }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public City getStartCity() {
        return startCity;
    }

    public void setStartCity(City startCity) {
        this.startCity = startCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public Plane getPlane() {
        return plane;
    }
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Flight[] getAll(Connection connection) {
        return this.read("ORDER BY start_date ASC", connection).toArray(Flight[]::new);
    }

    public Flight getById(Connection connection) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights).filter(f -> this.getId() == f.getId()).findFirst().orElse(null);
    }

    public Flight getByIdPlaneAndDate(Connection connection) {
        Flight[] flights = this.init(connection);
        LocalDate date = Utils.getDate(this.getStartDate());
        return Arrays.stream(flights).filter(f -> this.getIdPlane() == f.getIdPlane() && date.equals(Utils.getDate(f.getStartDate()))).findFirst().orElse(null);
    }

    public Flight[] init(Connection connection) {
        Flight[] flights = this.getAll(connection);

        if (flights != null && flights.length > 0) {
            for (Flight flight : flights) {
                Plane plane = new Plane(flight.getIdPlane());
                Plane plane1 = plane.getById(connection);
                PlaneModel planeModel = new PlaneModel(plane1.getIdModel());
                PlaneModel model = planeModel.getById(connection);
                plane1.setPlaneModel(model);
                flight.setPlane(plane1);

                City city = new City(flight.getIdStartCity());
                City city1 = city.getById(connection);
                flight.setStartCity(city1);

                City citydest = new City(flight.getIdDestinationCity());
                City city2 = citydest.getById(connection);
                flight.setDestinationCity(city2);
            }
        }

        return flights;
    }

    public void delete(Connection connection) {
        this.delete("id",connection);
    }

    public void update(Connection connection) {
        this.update("idPlane,idStartCity,idDestinationCity,start_date","id",connection);
    }

    public void save(Connection connection) {
        this.create(connection);
    }

    public void addFlight(Connection connection) throws Exception{
        Flight flight = this.getByIdPlaneAndDate(connection);
        if (flight == null){
            this.save(connection);
        } else {
            throw new Exception("The plane " + flight.getPlane().getPlaneModel().getName() + " is unvailable for the date " + Utils.getDate(flight.getStartDate()));
        }
    }

    public PlaceType[] getPrice(Connection connection) {
        // get plane place by id plane
        PlanePlace planePlace = new PlanePlace();
        planePlace.setIdPlane(this.getIdPlane());
        PlanePlace[] planePlaces = planePlace.getByIdPlane(connection);

        // get flight price by id flight
        FlightPrice flightPrice = new FlightPrice();
        flightPrice.setIdFlight(this.getId());
        FlightPrice[] flightPrices = flightPrice.getByIdFlight(connection);


        PlaceType[] result = new PlaceType[planePlaces.length];
        if (planePlaces != null && flightPrices != null) {
            for (int i = 0; i < planePlaces.length; i++) {
                for (FlightPrice price : flightPrices) {
                    if (planePlaces[i].getIdPlace() == price.getIdPlace()) {
                        PlaceType placeType = new PlaceType();
                        placeType.setId(planePlaces[i].getIdPlace());
                        placeType.setPrice(price.getPrice());
                        placeType.setName(planePlaces[i].getPlaceType().getName());
                        placeType.setAvailable(planePlaces[i].getNumber());

                        result[i] = placeType;
                    }
                }
            }
        }
        return result;
    }

    public PlaceType[] getPlanePlace(Connection connection) {
        // get plane place by id plane
        PlanePlace planePlace = new PlanePlace();
        planePlace.setIdPlane(this.getIdPlane());
        PlanePlace[] planePlaces = planePlace.getByIdPlane(connection);

        // get flight price by id flight
        PlaceType[] placeTypes = new PlaceType().getAll(connection);

        PlaceType[] result = new PlaceType[planePlaces.length];
        if (planePlaces != null && placeTypes != null) {
            for (int i = 0; i < planePlaces.length; i++) {
                for (PlaceType price : placeTypes) {
                    if (planePlaces[i].getIdPlace() == price.getId()) {
                        PlaceType placeType = new PlaceType();
                        placeType.setId(planePlaces[i].getIdPlace());
                        placeType.setName(planePlaces[i].getPlaceType().getName());
                        placeType.setAvailable(planePlaces[i].getNumber());

                        result[i] = placeType;
                    }
                }
            }
        }
        return result;
    }

    public PlaceType getPriceByIdPlace(Connection connection, int idPlace) {
        PlaceType[] places = this.getPrice(connection);
        return Arrays.stream(places)
                .filter(place -> place.getId() == idPlace)
                .findFirst()
                .orElse(null);
    }

    public int getNumberPlacesAvailable(Connection connection, int idPlace) {
        Booking booking = new Booking();
        booking.setIdFlight(this.getId());
        Booking[] bookings = booking.getByIdFlight(connection);
        int reserved = Arrays.stream(bookings).filter(book -> book.getIdPlace() == idPlace).mapToInt(Booking::getPlaceNumber).sum();

        PlanePlace planePlace = new PlanePlace();
        planePlace.setIdPlace(idPlace);
        planePlace.setIdPlane(this.getIdPlane());
        PlanePlace place = planePlace.getByIdPlaneAndIdPlace(connection);
        int total = place != null ? place.getNumber() : 0;

        return total - reserved;
    }

    public Flight[] getByStartCity(Connection connection, String city) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getStartCity().getName().toLowerCase().contains(city.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] getByDestinationCity(Connection connection, String city) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getDestinationCity().getName().toLowerCase().contains(city.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] getByPlane(Connection connection, int plane) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getPlane().getId() == plane)
                .toArray(Flight[]::new);
    }

    public Flight[] getByPlaneAndStartCity(Connection connection, int plane, String city) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getPlane().getId() == plane && flight.getStartCity().getName().toLowerCase().contains(city.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] getByPlaneAndDestCity(Connection connection, int plane, String city) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getPlane().getId() == plane && flight.getDestinationCity().getName().toLowerCase().contains(city.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] getByStartCityAndDestCity(Connection connection, String start, String dest) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getDestinationCity().getName().toLowerCase().contains(dest.toLowerCase()) && flight.getStartCity().getName().toLowerCase().contains(start.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] getByPlaneAndStartCityAndDestCity(Connection connection, int plane, String start, String dest) {
        Flight[] flights = this.init(connection);
        return Arrays.stream(flights)
                .filter(flight -> flight.getPlane().getId() == plane && flight.getDestinationCity().getName().toLowerCase().contains(dest.toLowerCase()) && flight.getStartCity().getName().toLowerCase().contains(start.toLowerCase()))
                .toArray(Flight[]::new);
    }

    public Flight[] search(Connection connection, String startCity, String destCity, String plane) {
        Flight[] flights;

        if (plane.equals("0") && startCity.equals("0") && destCity.equals("0")) {
            // Retourne tous les vols
            flights = this.init(connection);
        } else if (plane.equals("0") && !startCity.equals("0") && destCity.equals("0")) {
            // Recherche par ville de départ
            flights = this.getByStartCity(connection, startCity);
        } else if (plane.equals("0") && startCity.equals("0") && !destCity.equals("0")) {
            // Recherche par ville de destination
            flights = this.getByDestinationCity(connection, destCity);
        } else if (!plane.equals("0") && startCity.equals("0") && destCity.equals("0")) {
            // Recherche par avion uniquement
            flights = this.getByPlane(connection, Integer.parseInt(plane));
        } else if (plane.equals("0") && !startCity.equals("0") && !destCity.equals("0")) {
            // Recherche par ville de départ et destination
            flights = this.getByStartCityAndDestCity(connection, startCity, destCity);
        } else if (!plane.equals("0") && !startCity.equals("0") && destCity.equals("0")) {
            // Recherche par avion et ville de départ
            flights = this.getByPlaneAndStartCity(connection, Integer.parseInt(plane), startCity);
        } else if (!plane.equals("0") && startCity.equals("0") && !destCity.equals("0")) {
            // Recherche par avion et ville de destination
            flights = this.getByPlaneAndDestCity(connection, Integer.parseInt(plane), destCity);
        } else {
            // Recherche par avion, ville de départ et ville de destination
            flights = this.getByPlaneAndStartCityAndDestCity(connection, Integer.parseInt(plane), startCity, destCity);
        }

        return flights;
    }

}
