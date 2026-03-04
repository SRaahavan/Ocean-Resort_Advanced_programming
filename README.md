# 🏨 Ocean View Resort — Room Reservation System
**Module:** CIS6003 Advanced Programming | Cardiff Metropolitan University  
**Academic Year:** 2025, Semester 1

---

## 📋 Project Overview
A Java-based web application for managing room reservations at **Ocean View Resort**, Galle, Sri Lanka. Built as a distributed, 3-tier web application using Jakarta Servlets, JSP, MySQL, and JUnit 5 automated testing.

---

## 🏗️ Architecture — 3-Tier Design

```
Presentation Tier  →  JSP + CSS (WEB-INF/views/)
Business Tier      →  Service Layer (ReservationService.java)
Data Tier          →  DAO + MySQL (ReservationDAO.java + DBConnectionPool)
```

---

## 🎨 Design Patterns Used

| Pattern        | Class                        | Purpose                                      |
|---------------|------------------------------|----------------------------------------------|
| **Singleton**  | `DBConnectionPool`           | Single DB connection pool across app         |
| **DAO**        | `ReservationDAO`             | Separates DB logic from business logic        |
| **Facade**     | `ReservationService`         | Simplified interface to DAOs + business rules |
| **Filter**     | `AuthFilter`                 | Authentication/security chain                 |
| **MVC**        | Servlet + JSP                | Separates Controller, View, Model             |

---

## ⚙️ Prerequisites

- Java 11+
- Apache Maven 3.6+
- Apache Tomcat 10.x
- MySQL 8.0+

---

## 🚀 Setup Instructions

### Step 1 — MySQL Database Setup
```sql
-- Connect to MySQL as root, then run:
mysql -u root -p < database/schema.sql
```
This creates the database, tables, room types, and default users.

### Step 2 — Configure Database Connection
Edit `src/main/webapp/WEB-INF/web.xml` and update:
```xml
<context-param>
    <param-name>db.url</param-name>
    <param-value>jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&amp;serverTimezone=UTC</param-value>
</context-param>
<context-param>
    <param-name>db.username</param-name>
    <param-value>your_mysql_username</param-value>
</context-param>
<context-param>
    <param-name>db.password</param-name>
    <param-value>your_mysql_password</param-value>
</context-param>
```

### Step 3 — Build the WAR
```bash
cd hotel-reservation
mvn clean package
```
This produces: `target/ocean-view-resort.war`

### Step 4 — Deploy to Tomcat
Copy `ocean-view-resort.war` to your Tomcat `webapps/` directory and start Tomcat:
```bash
# macOS/Linux
$TOMCAT_HOME/bin/startup.sh

# Windows
%TOMCAT_HOME%\bin\startup.bat
```

### Step 5 — Access the Application
Open your browser and go to:
```
http://localhost:8080/ocean-view-resort/
```

---

## 🔑 Default Login Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | `admin`  | `admin123`|
| Staff | `staff`  | `staff123`|

---

## ✅ Running Automated Tests (Task C)

```bash
mvn test
```

Tests are written using **JUnit 5** and **Mockito** (no real DB needed for tests).

### Test Summary (25 Test Cases)
- Authentication validation (T01–T06)
- Reservation field validation (T07–T11)  
- Phone number format validation — parameterized (T12–T13)
- Date business rules (T14–T17)
- Billing calculations (T18–T20)
- Edge cases and error handling (T21–T25)

---

## 📁 Project Structure
```
hotel-reservation/
├── pom.xml
├── database/
│   └── schema.sql              ← MySQL setup script
├── src/
│   ├── main/
│   │   ├── java/com/oceanview/
│   │   │   ├── model/          ← Reservation.java, RoomType.java
│   │   │   ├── dao/            ← ReservationDAO.java
│   │   │   ├── service/        ← ReservationService.java (Facade)
│   │   │   ├── servlet/        ← All servlets
│   │   │   ├── filter/         ← AuthFilter.java
│   │   │   └── util/           ← DBConnectionPool.java (Singleton)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── views/      ← JSP pages
│   │       └── css/
│   │           └── style.css
│   └── test/
│       └── java/com/oceanview/
│           └── ReservationServiceTest.java  ← 25 JUnit 5 tests
└── README.md
```

---

## 🗄️ Database Schema

| Table          | Purpose                             |
|----------------|-------------------------------------|
| `admin`        | Staff login credentials             |
| `room_types`   | Room types and nightly rates        |
| `reservations` | All guest reservation records       |

---

## 🌐 Application Pages

| URL                 | Description                      |
|---------------------|----------------------------------|
| `/login`            | Login page                       |
| `/dashboard`        | Main dashboard with stats        |
| `/reservations`     | All reservations + search        |
| `/add-reservation`  | New reservation form             |
| `/view-reservation` | Detailed reservation view        |
| `/bill`             | Printable invoice/bill           |
| `/help`             | Staff user guide                 |
| `/logout`           | Logout and end session           |

---

## 🏷️ Features Implemented

✅ Secure login with session management  
✅ Auto-generated reservation numbers (RES-YYYY####)  
✅ Full guest registration (name, address, contact, email)  
✅ Room type selection with live cost preview  
✅ Date validation (no past dates, check-out after check-in)  
✅ Bill calculation with 10% tax + printable invoice  
✅ Status management (Confirmed → Checked In → Checked Out/Cancelled)  
✅ Search reservations by name or reservation number  
✅ Today's check-in dashboard widget  
✅ Dashboard statistics  
✅ Help/user guide page  
✅ Dialog-based error messages  
✅ Sticky form data on validation errors  
✅ 25 automated JUnit 5 tests (TDD approach)  
✅ Singleton DB connection pool  
✅ DAO, Facade, Filter, MVC design patterns  
✅ 3-tier architecture  
✅ Responsive UI  

---

*Ocean View Resort, Galle, Sri Lanka © 2025*
