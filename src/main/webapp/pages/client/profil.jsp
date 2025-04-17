<%@ page import="mg.ticketing.models.person.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String root = request.getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    String picture = user.getPicture() == null || user.getPicture().isEmpty() ? "profil.png" : user.getPicture();
    String message = request.getParameter("message");
%>
<html>
<head>
    <title>SkyBooking</title>
    <link rel="stylesheet" href="<%=root %>/assets/client/styles/profil.css">
    <script>
        <% if (!message.isEmpty()) { %>
            alert("<%=message%>");
        <% }%>
    </script>
</head>
<body>
<nav class="navbar">
    <div class="logo">SkyBooking</div>
    <div class="user-nav">
        <form action="<%=root%>/app/flights" method="post">
            <input type="hidden" name="message" value="">
            <button type="submit">Back</button>
        </form>
        <a href="<%=request.getContextPath()%>/app/user/logout" class="logout-btn">Logout</a>
    </div>
</nav>
<section>
    <div class="container">
        <div class="profil">
            <div class="profil-info">
                <h1>Profil</h1>
                <div class="profil-info-content">
                    <div class="profil-info-content-item">
                        <div class="profil-photo-container">
                            <img src="<%=root %>/assets/client/images/<%=picture %>" alt="Profil" class="profil-photo">
                            <form id="uploadForm" action="<%=root%>/app/user/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="profileInput" name="picture" class="file-input" accept="image/*">
                                <input type="hidden" name="id" value="<%=user.getId()%>">
                                <p id="file-name">Upload profil picture</p>
                                <button type="submit" id="uploadBtn" class="upload-btn">Update</button>
                            </form>
                        </div>
                    </div>
                    <div class="profil-info-content-item">
                        <p>Username</p>
                        <strong><%=user.getUsername()%></strong>
                    </div>
                    <div class="profil-info-content-item">
                        <p>Email</p>
                        <strong><%=user.getEmail()%></strong>
                    </div>
                </div>

            </div>
        </div>
    </div>
</section>

<script>
    document.getElementById("profileInput").addEventListener("change", function(event) {
        const file = event.target.files[0];
        if (file) {
            console.log("Fichier sélectionné :", file.name);
            document.getElementById("uploadBtn").style.display = "block";
            document.getElementById("file-name").innerHTML = "File : " + file.name;
        }
    });
</script>

</body>
</html>
