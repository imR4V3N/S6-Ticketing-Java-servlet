<%@ page import="mg.ticketing.models.flight.Flight" %>
<%@ page import="mg.ticketing.models.plane.PlaceType" %>
<%@ page import="mg.ticketing.models.flight.FlightPromotion" %>
<%@ page import="mg.ticketing.models.flight.Booking" %>
<%@ page import="mg.ticketing.models.utils.Utils" %>
<%@ page import="mg.ticketing.models.person.Age" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String root = request.getContextPath();

    FlightPromotion[] promotions = (FlightPromotion[]) request.getAttribute("promotions");
    PlaceType[] placeTypes = (PlaceType[]) request.getAttribute("placeTypes");
    Booking[] bookings = (Booking[]) request.getAttribute("bookings");
    Flight flight = (Flight) request.getAttribute("flight");
    Age[] ages = (Age[]) request.getAttribute("ages");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <title>SkyBooking</title>
    <link rel="stylesheet" href="<%=root%>/assets/client/styles/booking.css">
    <link rel="stylesheet" href="<%=root%>/assets/client/font/font.css">
    <link rel="stylesheet" href="<%=root%>/assets/client/styles/ticket.css">
    <script src="<%=root%>/assets/client/js/popup.js"></script>
    <script>
        <% if (!message.isEmpty()) { %>
            alert("<%=message%>");
        <% }%>
    </script>
</head>
<body>
<div class="header">
    <h1>Flight from <%=flight.getStartCity().getName()%> to <%=flight.getDestinationCity().getName()%></h1>
    <nav>
        <form action="<%=root%>/app/flights" method="post">
            <input type="hidden" name="message" value="">
            <button type="submit">Back</button>
        </form>
        <a href="user/logout" class="logout-btn">Logout</a>
    </nav>
</div>

<div class="card">
    <h2>Flight Details</h2>
    <p><strong>Date:</strong> <%=flight.getStartDate()%></p>
</div>

<% if (promotions != null && promotions.length > 0) { %>
<div class="card">
    <h2>Current Promotions</h2>
    <table>
        <thead>
        <tr>
            <th>Class</th>
            <th>Place number</th>
            <th>Promotion</th>
        </tr>
        </thead>
        <tbody>
        <% for (FlightPromotion value : promotions){ %>
        <tr>
            <td><%=value.getPlaceType().getName() %></td>
            <td><%=value.getPlaceNumber() %></td>
            <td><%=value.getPromotion() %>%</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
<% } else { %>
<div class="empty-state">
    <p>No current promotions available</p>
</div>
<% } %>

<% if (placeTypes != null && placeTypes.length > 0) { %>
<div class="card">
    <h2>Available Places</h2>
    <table>
        <thead>
        <tr>
            <th>Class</th>
            <th>Places Available</th>
            <th>Price</th>
        </tr>
        </thead>
        <tbody>
        <% for (PlaceType place : placeTypes){ %>
        <tr>
            <td><%=place.getName() %></td>
            <td><%=place.getAvailable() %></td>
            <td><%=place.getPrice()%> Ar</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
<% } %>

<div class="card">
    <h2>Make a Reservation</h2>
    <form action="<%= root %>/app/flights/booking" method="POST" enctype="multipart/form-data">
        <div class="form-group">
            <label for="placeType">Travel Class</label>
            <select id="placeType" name="place">
                <% if (placeTypes != null && placeTypes.length > 0) {
                    for (PlaceType place : placeTypes){ %>
                <option value="<%=place.getId() %>"><%=place.getName() %></option>
                <% } }%>
            </select>
        </div>

        <div class="form-group">
            <label for="age">Age</label>
            <select id="age" name="age">
                <% if (ages != null && ages.length > 0) {
                    for (Age value : ages){ %>
                <option value="<%=value.getId() %>"><%=value.getName() %></option>
                <% } }%>
            </select>
        </div>

        <div class="form-group">
            <label for="number">Number of Seats</label>
            <input type="number" name="number" id="number" min="1">
        </div>

        <div class="form-group">
            <label for="date">Travel Date</label>
            <input type="datetime-local" name="date" id="date">
        </div>

        <div class="form-group">
            <label for="date">Passport</label>
            <input type="file" id="profileInput" name="picture" class="file-input" accept="image/*">
        </div>

        <input type="hidden" name="flight" value="<%=flight.getId() %>">
        <button type="submit">Confirm Reservation</button>
    </form>
</div>

<% if (bookings != null && bookings.length > 0) { %>
<div class="card-container">
    <h2>Your Bookings</h2>
    <% for (Booking value : bookings){ %>
        <div class="ticket">
            <div class="left-ticket">
                <div class="header">
                    <h2>SkyBooking</h2>
                </div>
                <div class="body">
                    <div class="destination">
                        <p><%=flight.getStartCity().getName() %></p>
                        <span class="material-icons-sharp">flight</span>
                        <p><%=flight.getDestinationCity().getName() %></p>
                    </div>
                    <div class="ticket-info">
                        <div class="info">
                            <span class="material-icons-sharp">calendar_month</span>
                            <div class="text-info">
                                <p>Date</p>
                                <h4><%=Utils.getDateAndTime(flight.getStartDate())[0] %></h4>
                            </div>
                        </div>
                        <div class="info">
                            <span class="material-icons-sharp">access_time</span>
                            <div class="text-info">
                                <p>Time</p>
                                <h4><%=Utils.getDateAndTime(flight.getStartDate())[1] %></h4>
                            </div>
                        </div>
                        <div class="info">
                            <span class="material-icons-sharp">star_outline</span>
                            <div class="text-info">
                                <p>Class</p>
                                <h4><%=value.getPlace().getName() %></h4>
                            </div>
                        </div>
                        <div class="info">
                            <span class="material-icons-sharp">local_airport</span>
                            <div class="text-info">
                                <p>Plane</p>
                                <h4><%=flight.getPlane().getPlaneModel().getName() %></h4>
                            </div>
                        </div>
                        <div class="info">
                            <span class="material-icons-sharp">airline_seat_recline_normal</span>
                            <div class="text-info">
                                <p>Number of Seats</p>
                                <h4><%=value.getPlaceNumber() %></h4>
                            </div>
                        </div>
                        <div class="info">
                            <span class="material-icons-sharp">person</span>
                            <div class="text-info">
                                <p>Age</p>
                                <h4><%=value.getAge().getName() %></h4>
                            </div>
                        </div>
                        <div class="info active">
                            <span class="material-icons-sharp">attach_money</span>
                            <div class="text-info">
                                <p>Total</p>
                                <h4><%=Utils.formatNumber(value.getPrice()) %> Ar</h4>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer">
                    <h3><%=value.getUser().getUsername() %></h3>
                    <p>BOARDING PASS FL<%=value.getId() %></p>
                </div>
            </div>
            <div class="right-ticket">
                <span class="material-icons-sharp close" data-popup="popup<%=value.getId() %>" id="closePopup">close</span>
                <form action="<%=root%>/app/flights/booking/cancel" method="post" id="popup<%=value.getId() %>">
                    <h2>Are you sure to cancel your reservation ?</h2>
                    <input type="hidden" name="booking" value="<%=value.getId() %>">
                    <input type="hidden" name="flight" value="<%=flight.getId() %>">
                    <input type="datetime-local" name="date">
                    <div class="button">
                        <button id="confirmYes" type="submit" class="btn btn-danger">Yes</button>
                        <button id="confirmNo" type="reset" class="btn btn-primary">No</button>
                    </div>
                </form>


                <div class="header">
                    <h2><%=value.getUser().getUsername() %></h2>
                    <p>BOARDING PASS FL<%=value.getId() %></p>
                </div>
                <div class="destination">
                    <div class="city">
                        <span class="material-icons-sharp">flight</span>
                        <div class="city-info">
                            <p>From</p>
                            <h4><%=flight.getStartCity().getName() %></h4>
                        </div>
                    </div>
                    <span class="dash"></span>
                    <span class="dash"></span>
                    <div class="city">
                        <span class="material-icons-sharp">pin_drop</span>
                        <div class="city-info">
                            <p>To</p>
                            <h4><%=flight.getDestinationCity().getName() %></h4>
                        </div>
                    </div>
                </div>
                <div class="ticket-info">
                    <div class="info">
                        <span class="material-icons-sharp">calendar_month</span>
                        <div class="text-info">
                            <p>Date</p>
                            <h4><%=Utils.getDateAndTime(flight.getStartDate())[0] %></h4>
                        </div>
                    </div>
                    <div class="info">
                        <span class="material-icons-sharp">access_time</span>
                        <div class="text-info">
                            <p>Time</p>
                            <h4><%=Utils.getDateAndTime(flight.getStartDate())[1] %></h4>
                        </div>
                    </div>
                    <div class="info">
                        <span class="material-icons-sharp">star_outline</span>
                        <div class="text-info">
                            <p>Class</p>
                            <h4><%=value.getPlace().getName() %></h4>
                        </div>
                    </div>
                    <div class="info">
                        <span class="material-icons-sharp">local_airport</span>
                        <div class="text-info">
                            <p>Plane</p>
                            <h4><%=flight.getPlane().getPlaneModel().getName() %></h4>
                        </div>
                    </div>
                    <div class="info">
                        <span class="material-icons-sharp">airline_seat_recline_normal</span>
                        <div class="text-info">
                            <p>Number of Seats</p>
                            <h4><%=value.getPlaceNumber() %></h4>
                        </div>
                    </div>
                    <div class="info">
                        <span class="material-icons-sharp">attach_money</span>
                        <div class="text-info">
                            <p>Total</p>
                            <h4><%=Utils.formatNumber(value.getPrice()) %> Ar</h4>
                        </div>
                    </div>
                </div>
                <div class="barcode"></div>
            </div>
        </div>
    <% } %>
</div>
<% } else { %>
<div class="empty-state">
    <p>You have no current bookings</p>
</div>
<% } %>
</body>
</html>
