<%@ page import="mg.ticketing.models.person.User" %>
<%@ page import="mg.ticketing.models.flight.Flight" %>
<%@ page import="mg.ticketing.models.plane.Plane" %>
<%@ page import="mg.ticketing.models.location.City" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String root = request.getContextPath();

    User user = new User();
    if(request.getSession().getAttribute("user") != null){
    user = (User) request.getSession().getAttribute("user");
    }

    Flight[] flights = (Flight[]) request.getAttribute("flights");
    Plane[] planes = (Plane[]) request.getAttribute("planes");
    String message = (String) request.getAttribute("message");
    String[] cities = (String[]) request.getAttribute("cities");

%>
<!DOCTYPE html>
<html lang="en">
<!-- index.html  21 Nov 2019 03:44:50 GMT -->
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
    <title>SkyBooking - Home</title>
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
<script>
    <% if (!message.isEmpty()) { %>
        alert("<%=message%>");
    <% }%>
</script>
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
            <section class="section">
                <div class="section-body">
                    <!-- Table -->
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h4>Export Table</h4>
                                </div>
                                <div class="card-body">

                                    <!-- Recherche -->
                                    <div class="section-title">Search flight</div>
                                    <form action="<%=root %>/app/flights/search" method="POST" class="form-row" >
                                        <div class="form-group p-1">
                                            <label>Plane</label>
                                            <div class="input-group" >
                                                <select name="plane" class="form-control select2">
                                                    <option value="0">Select plane</option>
                                                    <% for (Plane plane : planes){ %>
                                                    <option value="<%=plane.getId() %>"><%=plane.getPlaneModel().getName() %></option>
                                                    <% } %>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group p-1">
                                            <label>Start city</label>
                                            <div class="input-group">
                                                <select name="start" class="form-control select2">
                                                    <option value="0">Select city</option>
                                                    <% for (String city : cities){ %>
                                                    <option value="<%=city %>"><%=city %></option>
                                                    <% } %>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group p-1">
                                            <label>Destination city</label>
                                            <div class="input-group">
                                                <select name="dest" class="form-control select2">
                                                    <option value="0">Select city</option>
                                                    <% for (String city : cities){ %>
                                                    <option value="<%=city %>"><%=city %></option>
                                                    <% } %>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="card-footer p-1" style="display: flex; align-items: center;">
                                            <button class="btn btn-lg btn-icon btn-primary p-2" type="submit">
                                                <i class="fas fa-search" style="font-size: 16px;"></i>
                                            </button>
                                        </div>
                                    </form>

                                    <div class="table-responsive">
                                        <% if (flights != null && flights.length > 0) { %>
                                        <table class="table table-striped table-hover" id="tableExport" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Plane</th>
                                                <th>Start City</th>
                                                <th>Destination City</th>
                                                <th>Date</th>
                                                <th>Promotion</th>
                                                <th>Price</th>
                                                <th>Update</th>
                                                <th>Delete</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <% for (Flight flight : flights){ %>
                                            <tr>
                                                <td>Flight<%=flight.getId() %></td>
                                                <td><%=flight.getPlane().getPlaneModel().getName() %></td>
                                                <td><%=flight.getStartCity().getName() %></td>
                                                <td><%=flight.getDestinationCity().getName() %></td>
                                                <td><%=flight.getStartDate() %></td>
                                                <td><a href="<%=root%>/app/flights/planes?id=<%=flight.getId() %>" class="btn btn-primary">Update</a></td>
                                                <td><form action="<%=root %>/app/flights/delete" method="POST" style="display: inline;">
                                                    <input type="hidden" name="id" value="<%= flight.getId() %>">
                                                    <button type="submit" class="btn btn-danger btn-action"data-toggle="tooltip" title="Delete"><i class="fas fa-trash"></i></button>
                                                </form></td>
                                                <td><a href="<%=root%>/app/flights/promotions?id=<%=flight.getId() %>&message=" class="btn btn-info">Promotion</a></td>
                                                <td><a href="<%=root%>/app/flights/prices?id=<%=flight.getId() %>&message=" class="btn btn-info">Price</a></td>
                                            </tr>
                                            <% } %>
                                            </tbody>
                                        </table>
                                        <% } else { %>
                                        <div class="alert alert-warning alert-has-icon">
                                            <div class="alert-icon"><i class="far fa-lightbulb"></i></div>
                                            <div class="alert-body">
                                                <div class="alert-title">Warning</div>
                                                No flight found.
                                            </div>
                                        </div>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                </div>
            </section>


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
