package mg.ticketing.models.person;

import com.google.gson.annotations.Expose;
import mg.framework.dao.annotation.Column;
import mg.framework.dao.annotation.Table;
import mg.framework.dao.utils.Dao;

import java.sql.Connection;
import java.util.Arrays;

@Table(name = "age")
public class Age extends Dao {
    @Column(isPK = true)
    private int id;

    @Column
    private String name;

    @Column
    private int min_age;

    @Column
    private int max_age;

    @Column
    private double promotion;

    public Age() {}
    public Age(String name, int min_age, int max_age) {
        this.name = name;
        this.min_age = min_age;
        this.max_age = max_age;
    }
    public Age(int id, String name, int min_age, int max_age) {
        this.id = id;
        this.name = name;
        this.min_age = min_age;
        this.max_age = max_age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin_age() {
        return min_age;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public int getMax_age() {
        return max_age;
    }

    public void setMax_age(int max_age) {
        this.max_age = max_age;
    }

    public double getPromotion() {
        return promotion;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    public Age[] getAll(Connection connection) {
        return this.read("", connection).toArray(Age[]::new);
    }

    public Age getById(Connection connection) {
        return Arrays.stream(this.getAll(connection)).filter(age -> age.getId() == this.getId()).findFirst().orElse(null);
    }
}
