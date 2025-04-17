// City.java
package mg.ticketing.models.location;

import com.google.gson.annotations.Expose;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;
import mg.ticketing.models.plane.Plane;

import java.sql.Connection;
import java.util.Arrays;

@Table(name = "city")
public class City extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String name;

    public City() {}
    public City(int id) {
        this.id = id;
    }
    public City(String name) {
        this.name = name;
    }
    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public City getById(Connection connection) {
        return (City) this.read("WHERE id = " + this.getId(), connection).stream().findFirst().orElse(null);
    }

    public City[] getAll(Connection connection) {
        return this.read("ORDER BY name", connection).toArray(City[]::new);
    }

    public String[] getAllName(Connection connection){
        City[] cities = this.getAll(connection);
        return Arrays.stream(cities)
                .map(City::getName)
                .toArray(String[]::new);
    }

}
