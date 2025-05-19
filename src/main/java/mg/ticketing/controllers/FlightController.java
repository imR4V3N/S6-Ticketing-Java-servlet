package mg.ticketing.controllers;

import com.google.gson.Gson;
import jakarta.servlet.http.Part;
import mg.framework.annotation.servlet.*;
import mg.framework.servlet.ModelView;
import mg.framework.servlet.Session;
import mg.ticketing.models.flight.Booking;
import mg.ticketing.models.flight.Flight;
import mg.ticketing.models.flight.FlightPrice;
import mg.ticketing.models.flight.FlightPromotion;
import mg.ticketing.models.location.City;
import mg.ticketing.models.person.Age;
import mg.ticketing.models.person.User;
import mg.ticketing.models.plane.PlaceType;
import mg.ticketing.models.plane.Plane;
import mg.ticketing.models.utils.UtilDb;
import mg.ticketing.models.utils.Utils;
import java.sql.Connection;
import java.sql.Timestamp;

@Controller
public class FlightController {
    Session session;

    public void setSession(Session session){
        this.session = session;
    }

    @Url("flights")
    @Post
    public ModelView flights(@RequestParam("message") String message) {
        String page = "";
        ModelView model = new ModelView();
        Flight flight = new Flight();
        try {
            Connection connection = new UtilDb().getPgConnection();
            User user = (User) session.get("user");

            if (user.getRole().equalsIgnoreCase("admin")) {
                page = "pages/admin/main.jsp";
                String[] cities = new City().getAllName(connection);
                model.addData("cities", cities);
                model.addData("message", message);
            } else {
                String cities = new Gson().toJson(new City().getAllName(connection));
                model.addData("cities", cities);
                page = "pages/client/main.jsp";
            }

            Plane[] planes = new Plane().init(connection);
            model.addData("planes", planes);

            Flight[] flights = flight.init(connection);

            model.addData("flights", flights);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.setUrl(page);
        return model;
    }

    @Url("flights/search")
    @Post
    public ModelView search(@RequestParam("start") String start, @RequestParam("dest") String dest, @RequestParam("plane") String plane) {
        String page = "";
        ModelView model = new ModelView();
        Flight flight = new Flight();
        try {
            Connection connection = new UtilDb().getPgConnection();
            User user = (User) session.get("user");

            if (user.getRole().equalsIgnoreCase("admin")) {
                page = "pages/admin/main.jsp";
                model.addData("message", "");
            } else {
                page = "pages/client/main.jsp";
            }

            Plane[] planes = new Plane().init(connection);
            model.addData("planes", planes);

            Flight[] flights = flight.search(connection, start, dest, plane);
            model.addData("flights", flights);

            String[] cities = new City().getAllName(connection);
            model.addData("cities", cities);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.setUrl(page);
        return model;
    }

    @Url("flights/delete")
    @Post
    public ModelView delete(@RequestParam("id") String id) {
        ModelView model = new ModelView();
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(id));
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            flight.delete(connection);
            Flight[] flights = flight.init(connection);
            model.addData("flights", flights);
            message = "Flight deleted successfully";

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        model.setUrl("app/flights?message="+message);
        return model;
    }

    @Url("flights/planes")
    public ModelView planes(@RequestParam("id") int id) {
        ModelView model = new ModelView("pages/admin/update.jsp");
        Flight flight = new Flight();
        flight.setId(id);
        try {
            Connection connection = new UtilDb().getPgConnection();
            Flight flight1 = flight.getById(connection);
            model.addData("flight", flight1);

            Plane[] planes = new Plane().init(connection);
            model.addData("planes", planes);
            City[] cities = new City().getAll(connection);
            model.addData("cities", cities);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Url("flights/city")
    public ModelView city() {
        ModelView model = new ModelView("pages/admin/add.jsp");
        try {
            Connection connection = new UtilDb().getPgConnection();

            Plane[] planes = new Plane().init(connection);
            model.addData("planes", planes);
            City[] cities = new City().getAll(connection);
            model.addData("cities", cities);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Url("flights/update")
    @Post
    public ModelView update(@RequestParam("Flight") Flight flight, @RequestParam("date") String date) throws Exception {
        ModelView model = new ModelView();
        String message = "";
        String url = "app/flights";
        try {
            Connection connection = new UtilDb().getPgConnection();
            date = Utils.format(date);
            flight.setStartDate(Timestamp.valueOf(date));
            message = "Flight updated!";
            flight.update(connection);
            connection.close();
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        }
        model.setUrl(url+"?message="+message);
        return model;
    }

    @Url("flights/add")
    @Post
    public ModelView add(@RequestParam("Flight") Flight flight, @RequestParam("date") String date) throws Exception {
        ModelView model = new ModelView();
        String url = "app/flights";
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            date = Utils.format(date);

            flight.setStartDate(Timestamp.valueOf(date));
            flight.addFlight(connection);
            message = "Flight added successfully!";

            connection.close();
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.setUrl(url+"?message="+message);
        return model;
    }

    @Url("flights/promotion")
    @Post
    public ModelView promotion(@RequestParam("flight") String id, @RequestParam("message") String message) {
        ModelView model = new ModelView("pages/client/booking.jsp");
        try (Connection connection = new UtilDb().getPgConnection()) {
            int flightId = Integer.parseInt(id);
            User user = (User) session.get("user");

            // Récupération des données nécessaires
            Flight flight = new Flight();
            flight.setId(flightId);
            Flight flightDetails = flight.getById(connection);

            PlaceType[] placeTypes = getPlaceTypesWithAvailability(connection, flightDetails);
            FlightPromotion[] promotions = new FlightPromotion().getByIdFlight(connection);
            Booking[] bookings = getBookingsByUserAndFlight(connection, user, flightId);
            Age[] ages = new Age().getAll(connection);

            // Ajout des données au modèle
            model.addData("ages", ages);
            model.addData("placeTypes", placeTypes);
            model.addData("promotions", promotions);
            model.addData("bookings", bookings);
            model.addData("flight", flightDetails);
            model.addData("message", message != null ? message : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private PlaceType[] getPlaceTypesWithAvailability(Connection connection, Flight flight) throws Exception {
        PlaceType[] placeTypes = flight.getPrice(connection);
        if (placeTypes != null) {
            for (PlaceType placeType : placeTypes) {
                placeType.setAvailable(flight.getNumberPlacesAvailable(connection, placeType.getId()));
            }
        }
        return placeTypes;
    }

    private Booking[] getBookingsByUserAndFlight(Connection connection, User user, int flightId) throws Exception {
        Booking booking = new Booking();
        booking.setIdUser(user.getId());
        booking.setIdFlight(flightId);
        return booking.getByIdUserAndIdFlight(connection);
    }

    @Url("flights/booking")
    @Post
    public ModelView booking(@RequestParam("Booking") Booking booking, @RequestParam("flight") String idflight , @RequestParam("date") String date, @RequestParam("picture") Part picture) {
        ModelView model = new ModelView();
        String url = "app/flights/promotion";
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(idflight));
        User user = (User) session.get("user");
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            booking.book(connection, date, flight, user, picture);
            message = "Your reservation has been made";
            connection.close();
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        }
        model.setUrl(url+"?message="+message+"&&flight="+idflight);
        return model;
    }

    @Url("flights/booking/cancel")
    @Post
    public ModelView cancel(@RequestParam("booking") String id, @RequestParam("date") String date, @RequestParam("flight") String idflight) {
        ModelView model = new ModelView();
        String url = "app/flights/promotion";
        String message = "";
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(idflight));
        try {
            Connection connection = new UtilDb().getPgConnection();
            new Booking().cancelBooking(connection, id, date, flight);
            message = "Your reservation was canceled";
            connection.close();
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.setUrl(url+"?message="+message+"&&flight="+idflight);
        return model;
    }

    @Url("flights/promotions")
    public ModelView promotions(@RequestParam("id") String id, @RequestParam("message") String message) {
        ModelView model = new ModelView();
        String url = "pages/admin/promotion.jsp";
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(id));
        FlightPromotion promotion = new FlightPromotion();
        promotion.setIdFlight(Integer.valueOf(id));
        try {
            Connection connection = new UtilDb().getPgConnection();
            flight = flight.getById(connection);
            model.addData("flight", flight);
            FlightPromotion[] promotions = promotion.getByIdFlight(connection);
            model.addData("promotions", promotions);
            PlaceType[] places = flight.getPlanePlace(connection);
            model.addData("places", places);
            model.addData("message", message);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUrl(url);
        return model;
    }

    @Url("flights/promotions/add")
    public ModelView addPromotion(@RequestParam("Promotion") FlightPromotion promotion) {
        ModelView model = new ModelView();
        String url = "app/flights/promotions";
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(promotion.getIdFlight()));

        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();

            promotion.verifyPlace(flight.getById(connection), connection);
            FlightPromotion oldProm = promotion.getByIdFlightAndIdPlace(connection);

            if (oldProm == null) {
                message = "Promotion added";
                promotion.save(connection);
            } else {
                promotion.setId(oldProm.getId());
                promotion.update2(connection);
                message = "Promotion updated";
            }

            connection.close();
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.setUrl(url+"?id="+promotion.getIdFlight()+"&message="+message);
        return model;
    }

    @Url("flights/promotions/delete")
    public ModelView deletePromotion(@RequestParam("id") String id, @RequestParam("flight") String idFlight) {
        ModelView model = new ModelView();
        String url = "app/flights/promotions";
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            int idProm = Integer.valueOf(id);

            FlightPromotion flightPromotion = new FlightPromotion();
            flightPromotion.setId(idProm);
            flightPromotion.delete(connection);
            message = "Promotion deleted";

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUrl(url+"?id="+idFlight+"&message="+message);
        return model;
    }

    @Url("flights/prices")
    public ModelView prices(@RequestParam("id") String id, @RequestParam("message") String message) {
        ModelView model = new ModelView();
        String url = "pages/admin/price.jsp";
        Flight flight = new Flight();
        flight.setId(Integer.valueOf(id));
        FlightPrice price = new FlightPrice();
        price.setIdFlight(Integer.valueOf(id));
        try {
            Connection connection = new UtilDb().getPgConnection();
            flight = flight.getById(connection);
            model.addData("flight", flight);
            FlightPrice[] prices = price.getByIdFlight(connection);
            model.addData("prices", prices);
            PlaceType[] places = flight.getPlanePlace(connection);
            model.addData("places", places);
            model.addData("message", message);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUrl(url);
        return model;
    }

    @Url("flights/prices/add")
    public ModelView addPrice(@RequestParam("id") int idFlight, @RequestParam("place") int idPlace, @RequestParam("price") int price) {
        ModelView model = new ModelView();
        String url = "app/flights/prices";
        Flight flight = new Flight();
        flight.setId(idFlight);
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            FlightPrice prices = new FlightPrice(idFlight, idPlace, price);
            FlightPrice oldPrice = prices.getByIdPlaceAndIdFlight(connection);
            if (oldPrice == null) {
                message = "Price added";
                prices.save(connection);
            } else {
                prices.setId(oldPrice.getId());
                prices.update(connection);
                message = "Price updated";
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUrl(url+"?id="+idFlight+"&message="+message);
        return model;
    }

    @Url("flights/prices/delete")
    public ModelView deletePrice(@RequestParam("id") String id, @RequestParam("flight") String idFlight) {
        ModelView model = new ModelView();
        String url = "app/flights/prices";
        String message = "";
        try {
            Connection connection = new UtilDb().getPgConnection();
            int idPrice = Integer.valueOf(id);

            FlightPrice price = new FlightPrice();
            price.setId(idPrice);
            price.delete(connection);
            message = "Price deleted";

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUrl(url+"?id="+idFlight+"&message="+message);
        return model;
    }
}
