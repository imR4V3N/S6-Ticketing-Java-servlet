// PlanePlace.java
package mg.ticketing.models.plane;

import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.flight.Flight;
import mg.ticketing.models.location.City;

import java.sql.Connection;
import java.util.Arrays;

@Table(name = "plane_place")
public class PlanePlace extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idPlace;
    @Column
    private int idPlane;
    @Column
    private int number;
    private PlaceType placeType;
    private Plane plane;

    public PlanePlace() {}
    public PlanePlace(int idPlace, int idPlane, int number) {
        this.idPlace = idPlace;
        this.idPlane = idPlane;
        this.number = number;
    }
    public PlanePlace(int id, int idPlace, int idPlane, int number) {
        this.id = id;
        this.idPlace = idPlace;
        this.idPlane = idPlane;
        this.number = number;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPlace() { return idPlace; }
    public void setIdPlace(int idPlace) { this.idPlace = idPlace; }
    public int getIdPlane() { return idPlane; }
    public void setIdPlane(int idPlane) { this.idPlane = idPlane; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public PlanePlace[] getAll(Connection connection) {
        return this.read("", connection).toArray(PlanePlace[]::new);
    }

    public PlanePlace[] init(Connection connection) {
        PlanePlace[] places = this.getAll(connection);

        if (places.length > 0 && places != null) {
            for (PlanePlace place : places) {
                Plane plane = new Plane(place.getIdPlane());
                Plane plane1 = plane.getById(connection);
                PlaneModel planeModel = new PlaneModel(plane1.getIdModel());
                PlaneModel model = planeModel.getById(connection);
                plane1.setPlaneModel(model);

                place.setPlane(plane1);

                PlaceType placeType = new PlaceType();
                placeType.setId(place.getIdPlace());
                PlaceType placeType1 = placeType.getById(connection);

                place.setPlaceType(placeType1);
            }
        }

        return places;
    }

    public PlanePlace[] getByIdPlane(Connection connection) {
        PlanePlace[] planePlaces = this.init(connection);
        return Arrays.stream(planePlaces).filter(p -> p.getIdPlane() == this.getIdPlane()).toArray(PlanePlace[]::new);
    }

    public PlanePlace getByIdPlaneAndIdPlace(Connection connection) {
        PlanePlace[] planePlaces = this.init(connection);
        return Arrays.stream(planePlaces).filter(p -> p.getIdPlane() == this.getIdPlane() && p.getIdPlace() == this.getIdPlace()).findFirst().orElse(null);
    }
}
