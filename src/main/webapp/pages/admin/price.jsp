<%@ page import="mg.ticketing.models.flight.FlightPromotion" %>
<%@ page import="mg.ticketing.models.plane.PlaceType" %>
<%@ page import="mg.ticketing.models.flight.Flight" %>
<%@ page import="mg.ticketing.models.flight.FlightPrice" %>
<%--
  Created by IntelliJ IDEA.
  User: raven
  Date: 16/02/2025
  Time: 12:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String root = request.getContextPath();

    FlightPrice[] prices = (FlightPrice[]) request.getAttribute("prices");
    PlaceType[] places = (PlaceType[]) request.getAttribute("places");
    Flight flight = (Flight) request.getAttribute("flight");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="en">
<!-- index.html  21 Nov 2019 03:44:50 GMT -->
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
    <title>SkyBooking - Price</title>
    <!-- General CSS Files -->
    <link rel="stylesheet" href="<%=root%>/assets/css/app.min.css">

    <!-- Table -->
    <link rel="stylesheet" href="<%=root%>/assets/bundles/datatables/datatables.min.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">

    <!-- Form -->
    <link rel="stylesheet" href="<%=root%>/assets/bundles/bootstrap-daterangepicker/daterangepicker.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/select2/dist/css/select2.min.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/jquery-selectric/selectric.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/pretty-checkbox/pretty-checkbox.min.css">

    <!-- Template CSS -->
    <link rel="stylesheet" href="<%=root%>/assets/css/style.css">
    <link rel="stylesheet" href="<%=root%>/assets/css/components.css">
    <!-- Custom style CSS -->
    <link rel="stylesheet" href="<%=root%>/assets/css/custom.css">
    <link rel='shortcut icon' type='image/x-icon' href='<%=root%>/assets/img/favicon.ico' />
</head>
<body>
<div class="loader"></div>
<div id="app">
    <div class="main-wrapper main-wrapper-1">
        <div class="navbar-bg"></div>
        <!--   HEADER     -->
        <jsp:include page="../static/header.jsp"></jsp:include>

        <!--   SIDEBAR     -->
        <jsp:include page="../static/sidebar.jsp"></jsp:include>


        <!-- MAIN CONTENT -->
        <div class="main-content">
            <!-- Formulaire -->
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4>Add Price for the <%=flight.getPlane().getPlaneModel().getName() %> from <%=flight.getStartCity().getName()%> to <%=flight.getDestinationCity().getName()%></h4>
                        </div>
                        <div class="card-body">
                            <form action="<%=root%>/app/flights/prices/add" method="get">
                                <input type="hidden" name="id" value="<%=flight.getId() %>">

                                <div class="section-title">Plane</div>
                                <div class="form-group">
                                    <label>Class</label>
                                    <select class="form-control select2" name="place">
                                        <% for (PlaceType value : places) { %>
                                        <option value="<%=value.getId()%>"><%=value.getName()%></option>
                                        <% } %>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Price</label>
                                    <input type="text" name="price" class="form-control">
                                </div>
                                <button class="btn btn-primary" type="submit">Add</button>
                            </form>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <% if (prices != null && prices.length > 0) { %>
                        <table class="table table-striped table-hover" id="tableExport" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>Place</th>
                                <th>Price</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (FlightPrice value : prices){ %>
                            <tr>
                                <td><%=value.getId() %></td>
                                <td><%=value.getPlaceType().getName() %></td>
                                <td><%=value.getPrice() %> Ar</td>
                                <td class="actions">
                                    <form action="<%=root %>/app/flights/prices/delete" method="get" style="display: inline;">
                                        <input type="hidden" name="flight" value="<%=flight.getId() %>">
                                        <input type="hidden" name="id" value="<%=value.getId() %>">
                                        <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                        <div class="alert alert-warning alert-has-icon">
                            <div class="alert-icon"><i class="far fa-lightbulb"></i></div>
                            <div class="alert-body">
                                <div class="alert-title">Warning</div>
                                No current price.
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- SETTING -->
            <jsp:include page="../static/setting.jsp"></jsp:include>

        </div>

        <!--   FOOTER     -->
        <jsp:include page="../static/footer.jsp"></jsp:include>

    </div>
</div>
<!-- General JS Scripts -->
<script src="<%=root%>/assets/js/app.min.js"></script>

<!-- Table -->
<script src="<%=root%>/assets/bundles/datatables/datatables.min.js"></script>
<script src="<%=root%>/assets/js/page/datatables.js"></script>
<script src="<%=root%>/assets/bundles/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>

<!-- JS Libraies -->
<script src="<%=root%>/assets/bundles/jquery-ui/jquery-ui.min.js"></script>
<script src="<%=root%>/assets/bundles/cleave-js/dist/cleave.min.js"></script>
<script src="<%=root%>/assets/bundles/cleave-js/dist/addons/cleave-phone.us.js"></script>
<script src="<%=root%>/assets/bundles/jquery-pwstrength/jquery.pwstrength.min.js"></script>
<script src="<%=root%>/assets/bundles/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="<%=root%>/assets/bundles/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
<script src="<%=root%>/assets/bundles/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
<script src="<%=root%>/assets/bundles/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
<script src="<%=root%>/assets/bundles/select2/dist/js/select2.full.min.js"></script>
<script src="<%=root%>/assets/bundles/jquery-selectric/jquery.selectric.min.js"></script>


<!-- Auto-complete -->
<script src="<%=root%>/assets/js/page/forms-advanced-forms.js"></script>

<!-- Page Specific JS File -->
<script src="<%=root%>/assets/js/page/index.js"></script>
<!-- Template JS File -->
<script src="<%=root%>/assets/js/scripts.js"></script>
<!-- Custom JS File -->
<script src="<%=root%>/assets/js/custom.js"></script>
</body>

<!-- index.html  21 Nov 2019 03:47:04 GMT -->
</html>
