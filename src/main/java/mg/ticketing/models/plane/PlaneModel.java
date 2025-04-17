// PlaneModel.java
package mg.ticketing.models.plane;

import com.google.gson.annotations.Expose;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;

import java.sql.Connection;

@Table(name = "plane_model")
public class PlaneModel extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String name;

    public PlaneModel() {}
    public PlaneModel(int id) {
        this.id = id;
    }
    public PlaneModel(String name) {
        this.name = name;
    }
    public PlaneModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PlaneModel getById(Connection connection) {
        return (PlaneModel) this.read("WHERE id = " + this.getId(), connection).stream().findFirst().orElse(null);
    }
}
