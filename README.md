# ✈️ Ticketing - Java Servlet (S4-Framework)

**Ticketing** is a web application for flight booking developed using **Java Servlets**, based on the lightweight framework [S4-Framework-Java](https://github.com/imR4V3N/S4-Framework-Java).

## 🔧 Technologies Used

- Java Servlet (Jakarta EE)
- S4-Framework-Java
- JSP / HTML / CSS / JavaScript
- PostgreSQL 
- Bootstrap (UI)
- iText PDF (for PDF export)
- Tomcat (or any servlet container)

---

## 📁 Features

### 🔐 Back Office (Admin Panel)

- 🔎 **Flight CRUD**
  - Create, update, delete flights
  - Multi-criteria search: departure, destination, date, airline, etc.

- 💸 **Promotion Management**
  - Add promotions per **class** (economy, business...) and per **flight**

- 💰 **Price Management**
  - Set prices per class per flight

---

### 🌐 Front Office (Client)

- ✈️ **Search & Display Available Flights**
  - Multi-criteria search: destination, date, etc.

- 🧾 **Flight Reservation**
  - Fill in passenger information
  - **Upload passport**

- 📄 **PDF Export (via REST API)**
  - Generate a ticket or booking confirmation as a PDF file

- 👤 **User Profile**
  - View booked flights
  - Update personal info

---

## ⚙️ Configuration

### 1. Requirements

Make sure you have:

- JDK 17+
- Tomcat 10+
- Maven
- PostgreSQL configured

### 2. `web.xml` Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <servlet>
        <servlet-name>FrontController</servlet-name>
        <servlet-class>mg.framework.controller.FrontController</servlet-class>
        <load-on-startup>1</load-on-startup>
        <init-param>
            <param-name>package_name</param-name>
            <param-value>mg.ticketing.controllers</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>FrontController</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>


    <context-param>
        <param-name>app.upload.path</param-name>
        <param-value>/home/raven/Workspace/Mr Naina/Ticketing/target/Ticketing-1.0-SNAPSHOT/assets/client/images/</param-value>
    </context-param>

    <context-param>
        <param-name>app.db.url</param-name>
        <param-value>jdbc:postgresql://localhost:5432/ticketing</param-value>
    </context-param>

    <context-param>
        <param-name>app.db.user</param-name>
        <param-value>postgres</param-value>
    </context-param>

    <context-param>
        <param-name>app.db.pwd</param-name>
        <param-value>post</param-value>
    </context-param>

    <listener>
        <listener-class>mg.ticketing.models.utils.Config</listener-class>
    </listener>
</web-app>
