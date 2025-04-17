package mg.ticketing.models.flight;

import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.plane.PlaceType;

import java.sql.Connection;
import java.util.Arrays;

@Table(name = "flight_price")
public class FlightPrice extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idFlight;
    @Column
    private int idPlace;
    @Column
    private double price;
    private Flight flight;
    private PlaceType placeType;

    public FlightPrice() {}
    public FlightPrice(int idFlight, int idPlace, double price) {
        this.idFlight = idFlight;
        this.idPlace = idPlace;
        this.price = price;
    }
    public FlightPrice(int id, int idFlight, int idPlace, double price) {
        this.id = id;
        this.idFlight = idFlight;
        this.idPlace = idPlace;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public FlightPrice[] getAll(Connection connection) {
        return this.read("", connection).toArray(FlightPrice[]::new);
    }

    public FlightPrice[] init(Connection connection) {
        FlightPrice[] results = this.getAll(connection);
        for (FlightPrice result : results) {
            PlaceType placeType = new PlaceType();
            placeType.setId(result.getIdPlace());
            result.setPlaceType(placeType.getById(connection));
        }
        return results;
    }

    public FlightPrice[] getByIdFlight(Connection connection) {
        FlightPrice[] results = Arrays.stream(this.init(connection)).filter(flightPrice -> flightPrice.getIdFlight() == this.getIdFlight()).toArray(FlightPrice[]::new);
        return results.length > 0 ? results : null;
    }

    public FlightPrice getByIdPlaceAndIdFlight(Connection connection) {
        return Arrays.stream(this.init(connection)).filter(flightPrice -> flightPrice.getIdPlace() == this.getIdPlace() && flightPrice.getIdFlight() == this.getIdFlight()).findFirst().orElse(null);
    }

    public void save(Connection connection) {
        this.create(connection);
    }

    public void delete(Connection connection) {
        this.delete("id", connection);
    }

    public void update(Connection connection) {
        this.update("price", "id", connection);
    }
}
