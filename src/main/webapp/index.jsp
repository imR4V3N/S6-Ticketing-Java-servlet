<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mg.framework.servlet.Validation" %>
<%
    String root = request.getContextPath();

    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - SkyBooking</title>
    <title>Otika - Admin Dashboard Template</title>
    <!-- General CSS Files -->
    <link rel="stylesheet" href="<%=root%>/assets/css/app.min.css">
    <link rel="stylesheet" href="<%=root%>/assets/bundles/bootstrap-social/bootstrap-social.css">
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
    <section class="section">
        <div class="container mt-5">
            <div class="row">
                <div class="col-12 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-6 offset-lg-3 col-xl-4 offset-xl-4">
                    <div class="card card-primary">
                        <div class="card-header">
                            <h4>Login</h4>
                        </div>
                        <div class="card-body">

                            <form action="<%=root%>/app/user/login" method="POST" class="needs-validation" novalidate="">
                                <% if (error != null){ %>
                                    <p class="text text-danger"><%=error %></p>
                                <% } %>
                                <div class="form-group">
                                    <label for="email">Email</label>
                                    <input id="text" type="email" class="form-control" name="User:email" value="<%=Validation.value("email", request) %>" tabindex="1" autofocus>
                                    <p class="text text-danger">
                                        <%=Validation.error("email", request) %>
                                    </p>
                                </div>
                                <div class="form-group">
                                    <div class="d-block">
                                        <label for="password" class="control-label">Password</label>
                                    </div>
                                    <input id="password" type="password" class="form-control" name="User:password" value="<%=Validation.value("password", request) %>" tabindex="2">
                                    <p class="text text-danger">
                                        <%=Validation.error("password", request) %>
                                    </p>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="btn btn-primary btn-lg btn-block" tabindex="4">
                                        Login
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<!-- General JS Scripts -->
<script src="<%=root%>/assets/js/app.min.js"></script>
<!-- JS Libraies -->
<!-- Page Specific JS File -->
<!-- Template JS File -->
<script src="<%=root%>/assets/js/scripts.js"></script>
<!-- Custom JS File -->
<script src="<%=root%>/assets/js/custom.js"></script>
</body>
</html>
