<%@ page import="mg.ticketing.models.person.User" %>
<%@ page import="mg.ticketing.models.flight.Flight" %>
<%@ page import="mg.ticketing.models.plane.PlaceType" %>
<%@ page import="mg.ticketing.models.plane.Plane" %><%--
  Created by IntelliJ IDEA.
  User: raven
  Date: 30/01/2025
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String root = request.getContextPath();

    User user = new User();
    if(request.getSession().getAttribute("user") != null){
        user = (User) request.getSession().getAttribute("user");
    }

    Flight[] flights = (Flight[]) request.getAttribute("flights");
    Plane[] planes = (Plane[]) request.getAttribute("planes");
    String cities = (String) request.getAttribute("cities");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SkyBooking - Find Your Next Flight</title>
    <link rel="stylesheet" href="<%= root %>/assets/client/styles/client.css">
    <link rel="stylesheet" href="<%= root %>/assets/client/styles/card.css">
    <link rel="stylesheet" href="<%= root %>/assets/client/font/font.css">
    <link rel="stylesheet" href="<%=root%>/assets/client/styles/jquery-ui.css">
    <script src="<%=root%>/assets/client/js/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <script>
        $(document).ready(function () {
            var cities = <%=cities%>;

            $("#cityInput").autocomplete({
                source: cities
            });
            $("#cityInput2").autocomplete({
                source: cities
            });
        });
    </script>
</head>
<body>
<nav class="navbar">
    <div class="logo">SkyBooking</div>
    <div class="user-nav">
        <span class="welcome-text">Welcome, <%=user.getUsername()%></span>
        <a href="<%=root%>/pages/client/profil.jsp?message=" class="logout-btn">Profil</a>
        <a href="user/logout" class="logout-btn">Logout</a>
    </div>
</nav>

<div class="container">
    <div class="hero">
        <h1>Find Your Perfect Flight</h1>
        <p>Discover amazing destinations and book your next adventure</p>
    </div>

    <div class="search-card">
        <form action="<%=root %>/app/flights/search" method="POST" class="search-form">
            <select name="plane">
                <option value="0">Select plane</option>
                <% for (Plane plane : planes){ %>
                <option value="<%=plane.getId() %>"><%=plane.getPlaneModel().getName() %></option>
                <% } %>
            </select>
            <input type="text" name="start" id="cityInput" placeholder="Start city name">
            <input type="text" name="dest" id="cityInput2" placeholder="Destination city name">
            <button type="submit" class="search-btn">Search</button>
        </form>
    </div>

    <div class="flights-section">
        <% if (flights != null && flights.length > 0) { %>
        <h2>Available Flights</h2>
        <div class="flights-grid">
            <% for (Flight flight : flights){ %>
            <div class="card">
                <div class="card-header">
                    <h3>SkyBooking</h3>
                </div>
                <div class="card-content">
                    <p class="card-date"><%=flight.getStartDate()%></p>
                    <div class="card-city">
                        <p><%=flight.getStartCity().getName()%></p>
                        <div class="icon">
                            <span>-</span>
                            <span class="material-icons-sharp">flight</span>
                            <span>-</span>
                        </div>
                        <p><%=flight.getDestinationCity().getName()%></p>
                    </div>
                    <div class="card-info">
                        <div class="info">
                            <span>Id flight</span>
                            <strong>FL<%=flight.getId()%></strong>
                        </div>
                        <div class="info">
                            <span>Plane</span>
                            <strong><%=flight.getPlane().getPlaneModel().getName()%></strong>
                        </div>
                    </div>
                </div>
                <form action="<%=root%>/app/flights/promotion" method="POST">
                    <input type="hidden" name="flight" value="<%=flight.getId()%>">
                    <input type="hidden" name="message" value="">
                    <button type="submit" class="booking-btn">Book Now</button>
                </form>
            </div>
            <% } %>
        </div>
        <% } else { %>
        <div class="empty-state">
            <h2>No Flights Available</h2>
            <p>Try adjusting your search criteria or check back later for new flights.</p>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
