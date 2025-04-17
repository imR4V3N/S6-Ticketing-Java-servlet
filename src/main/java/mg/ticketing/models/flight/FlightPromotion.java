// FlightPromotion.java
package mg.ticketing.models.flight;

import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.plane.PlaceType;
import mg.ticketing.models.plane.PlanePlace;

import java.sql.Array;
import java.sql.Connection;
import java.util.Arrays;


@Table(name = "flight_promotion")
public class FlightPromotion extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idFlight;
    @Column
    private int idPlace;
    @Column(name = "place_number")
    private int placeNumber;
    @Column
    private double promotion;
    private Flight flight;
    private PlaceType placeType;

    public FlightPromotion() {}
    public FlightPromotion(int idFlight, int idPlace, int placeNumber, double promotion) {
        this.idFlight = idFlight;
        this.idPlace = idPlace;
        this.placeNumber = placeNumber;
        this.promotion = promotion;
    }
    public FlightPromotion(int id, int idFlight, int idPlace, int placeNumber, double promotion) {
        this.id = id;
        this.idFlight = idFlight;
        this.idPlace = idPlace;
        this.placeNumber = placeNumber;
        this.promotion = promotion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdFlight() { return idFlight; }
    public void setIdFlight(int idFlight) { this.idFlight = idFlight; }
    public int getIdPlace() { return idPlace; }
    public void setIdPlace(int idPlace) { this.idPlace = idPlace; }
    public int getPlaceNumber() { return placeNumber; }
    public void setPlaceNumber(int placeNumber) { this.placeNumber = placeNumber; }
    public double getPromotion() { return promotion; }
    public void setPromotion(double promotion) { this.promotion = promotion; }

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

    public FlightPromotion[] getAll(Connection connection) {
        return this.read("", connection).toArray(FlightPromotion[]::new);
    }

    public FlightPromotion[] init(Connection connection) {
        FlightPromotion[] promotions = this.getAll(connection);
        if (promotions.length > 0 && promotions != null) {
            for (FlightPromotion promotion : promotions) {
                PlaceType placeType1 = new PlaceType();
                placeType1.setId(promotion.getIdPlace());
                promotion.setPlaceType(placeType1.getById(connection));
            }
        }
        return promotions;
    }

    public FlightPromotion[] getByIdFlight(Connection connection) {
        FlightPromotion[] promotions = this.init(connection);
        return Arrays.stream(promotions).filter(promotion -> promotion.getIdFlight() == this.getIdFlight() && promotion.getPlaceNumber() > 0).toArray(FlightPromotion[]::new);
    }

    public FlightPromotion getByIdFlightAndIdPlace(Connection connection) {
        FlightPromotion[] promotions = this.init(connection);
        return Arrays.stream(promotions)
                .filter(promotion -> promotion.getIdFlight() == this.getIdFlight() && promotion.getPlaceNumber() > 0 && promotion.getIdPlace() == this.getIdPlace())
                .findFirst()
                .orElse(null);
    }

    public void update(Connection connection) {
        this.update("place_number", "id", connection);
    }

    public void update2(Connection connection) {
        this.update("place_number,promotion", "id", connection);
    }

    public void delete(Connection connection) {
        this.delete("id", connection);
    }

    public void save(Connection connection) {
        this.create(connection);
    }

    public void verifyPlace(Flight flight, Connection connection) throws Exception {
        PlanePlace place = new PlanePlace();
        place.setIdPlace(this.getIdPlace());
        place.setIdPlane(flight.getIdPlane());
        place = place.getByIdPlaneAndIdPlace(connection);

        if (this.getPlaceNumber() > place.getNumber()) {
            throw new Exception("The maximum seat for this aircraft is "+place.getNumber()+" for the class " + place.getPlaceType().getName());
        }
        if (this.getPromotion() > 100) {
            throw new Exception("The promotion must be less than 100%");
        }
        if (this.getPromotion() < 0) {
            throw new Exception("The promotion must be greater than 0%");
        }
    }
}
