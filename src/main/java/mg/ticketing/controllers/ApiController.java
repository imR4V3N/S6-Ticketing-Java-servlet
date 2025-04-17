package mg.ticketing.controllers;

import mg.framework.annotation.servlet.*;
import mg.ticketing.models.flight.Booking;
import mg.ticketing.models.utils.UtilDb;
import mg.ticketing.models.utils.Utils;

import java.sql.Connection;

@Controller
@BaseUrl("api")
public class ApiController {
    @Url("booking")
    @RestAPI
    public byte[] booking(int id) {
        Booking booking = new Booking();
        booking.setId(id);
        byte[] bytes = new byte[0];
        try {
            Connection connection = new UtilDb().getPgConnection();
            booking = booking.getById(connection);
            bytes = Utils.exportPdf(booking);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
