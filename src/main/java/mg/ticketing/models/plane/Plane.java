// Plane.java
package mg.ticketing.models.plane;

import com.google.gson.annotations.Expose;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;

@Table(name = "plane")
public class Plane extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idModel;
    @Column(name = "fabrication_date")
    private Date fabricationDate;
    private PlaneModel planeModel;

    public Plane() {}
    public Plane(int id) {
        this.id = id;
    }
    public Plane(int idModel, Date fabricationDate) {
        this.idModel = idModel;
        this.fabricationDate = fabricationDate;
    }
    public Plane(int id, int idModel, Date fabricationDate) {
        this.id = id;
        this.idModel = idModel;
        this.fabricationDate = fabricationDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdModel() { return idModel; }
    public void setIdModel(int idModel) { this.idModel = idModel; }
    public Date getFabricationDate() { return fabricationDate; }
    public void setFabricationDate(Date fabricationDate) { this.fabricationDate = fabricationDate; }

    public PlaneModel getPlaneModel() {
        return planeModel;
    }

    public void setPlaneModel(PlaneModel planeModel) {
        this.planeModel = planeModel;
    }

    public Plane[] getAll(Connection connection) {
        return this.read("ORDER BY fabrication_date ASC", connection).toArray(Plane[]::new);
    }

    public Plane[] init(Connection connection) {
        Plane[] planes = this.getAll(connection);
        if (planes.length > 0 && planes != null) {
            for (Plane plane : planes) {
                PlaneModel planeModel = new PlaneModel(plane.getIdModel()).getById(connection);
                plane.setPlaneModel(planeModel);
            }
        }
        return planes;
    }

    public Plane getById(Connection connection) {
        Plane[] planes = this.init(connection);
        return Arrays.stream(planes).filter(plane -> plane.getId() == this.getId()).findFirst().orElse(null);
    }
}
