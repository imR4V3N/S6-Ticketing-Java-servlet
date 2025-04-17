package mg.ticketing;

import mg.ticketing.models.flight.Flight;
import mg.ticketing.models.location.City;
import mg.ticketing.models.person.User;
import mg.ticketing.models.plane.PlaceType;
import mg.ticketing.models.utils.UtilDb;
import mg.ticketing.models.utils.Utils;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public void loading(int dot, long time) throws Exception {
        for (int i = 0; i < dot; i++) {
            System.out.print(".");
            Thread.sleep(time);
        }
        System.out.println();
    }

    public static void restart() throws Exception {
        Connection connection = new UtilDb().getPgConnection();
        System.out.print("Restarting data");
        Utils.restartData(connection, "booking");
//        Utils.restartData(connection, "users");
//        Utils.restartData(connection, "city");
//        Utils.restartData(connection, "flight");
//        Utils.restartData(connection, "flight_price");
        Utils.restartData(connection, "flight_promotion");
//        Utils.restartData(connection, "place_type");
//        Utils.restartData(connection, "plane");
//        Utils.restartData(connection, "plane_model");
//        Utils.restartData(connection, "plane_place");
        Utils.restartData(connection, "age");
        connection.close();
        new Main().loading(3, 2000);
        System.out.println("Data restarted.");
    }

    public static void RandomData() throws Exception {
        Random random = new Random();
        double number = random.nextDouble((900 - 400) + 1) + 400;
        System.out.println(number * 4697);
    }

    public static void main(String[] args) throws Exception {
        restart();
//        Connection connection = new UtilDb().getPgConnection();
//        System.out.println();
//        connection.close();
    }
}
