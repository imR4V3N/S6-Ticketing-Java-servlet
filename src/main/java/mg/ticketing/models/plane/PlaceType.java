// PlaceType.java
package mg.ticketing.models.plane;

import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;

import java.sql.Connection;

@Table(name = "place_type")
public class PlaceType extends Dao {
        @Column(isPK = true)
        private int id;
        @Column
        private String name;
        private double price;
        private int available;

    public PlaceType() {}
    public PlaceType(String name) {
        this.name = name;
    }
    public PlaceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public PlaceType getById(Connection connection) {
        return (PlaceType) this.read("WHERE id = " + this.getId(),connection).stream().findFirst().orElse(null);
    }

    public PlaceType[] getAll(Connection connection) {
        return this.read("",connection).toArray(PlaceType[]::new);
    }
}
