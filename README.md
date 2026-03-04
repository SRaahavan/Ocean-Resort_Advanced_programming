# Ocean View Resort – Room Reservation System
CIS6003 Advanced Programming | Cardiff Metropolitan University

A full-stack Java web application for managing hotel room reservations.

## Tech Stack
- Java 17 | Jakarta Servlet 5.0 | JSP/JSTL 3.0
- MySQL 8.0 | Apache Tomcat 11
- Maven | JUnit 5 | Mockito 5.14.2

## Features
- Secure login with role-based access (Admin & Staff)
- Add / View / Search reservations
- Auto-generated reservation numbers (RES-YYYYNNNN)
- Bill calculation with 10% service tax
- Status management (CONFIRMED → CHECKED_IN → CHECKED_OUT)
- Dashboard with live statistics

## Setup
1. Create MySQL database: ocean_view_resort
2. Run SQL scripts in src/main/resources/
3. mvn clean package
4. Deploy WAR to Tomcat 11

## Default Credentials
- Admin: admin / admin123
- Staff: staff / staff123
