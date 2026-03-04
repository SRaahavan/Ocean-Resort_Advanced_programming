<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help – Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">❓ Help & User Guide</h1>
            <p class="page-subtitle">Step-by-step guide for Ocean View Resort Reservation System</p>
        </div>
    </div>

    <div class="alert alert-info">
        ℹ️ This guide is intended for hotel reception and management staff. For technical issues, contact the system administrator.
    </div>

    <div class="help-grid">
        <div class="help-card">
            <div class="help-icon">🔐</div>
            <h3>1. Logging In</h3>
            <p>Enter your assigned <strong>username</strong> and <strong>password</strong> on the login screen. The system has two roles: <em>Admin</em> and <em>Staff</em>. Contact your administrator if you cannot log in. Sessions expire after 30 minutes of inactivity for security.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">🏠</div>
            <h3>2. Dashboard Overview</h3>
            <p>The dashboard shows a live summary of all reservations: total bookings, confirmed, checked in, checked out, and cancelled. It also highlights <strong>today's check-ins</strong> so you can quickly assist arriving guests.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">➕</div>
            <h3>3. Adding a Reservation</h3>
            <p>Click <strong>"New Reservation"</strong> from the navbar or dashboard. Fill in the guest's full name, contact number, address, room type, and dates. The system auto-generates a unique <strong>Reservation Number</strong> (e.g., RES-20250001) and calculates the total cost automatically.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">🔍</div>
            <h3>4. Finding a Reservation</h3>
            <p>Go to <strong>Reservations</strong> in the menu. Use the search box to find a guest by name or reservation number. Click <strong>View</strong> on any row to see full booking details.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">🔄</div>
            <h3>5. Updating Reservation Status</h3>
            <p>On the reservation detail page, use the <strong>status dropdown</strong> to update it to:
                <br>• <em>Confirmed</em> – Initial booking state
                <br>• <em>Checked In</em> – Guest has arrived
                <br>• <em>Checked Out</em> – Guest has left
                <br>• <em>Cancelled</em> – Booking was cancelled
            </p>
        </div>

        <div class="help-card">
            <div class="help-icon">🧾</div>
            <h3>6. Generating a Bill</h3>
            <p>Click the <strong>"Bill"</strong> button on any reservation. The system calculates the total cost based on <strong>room rate × number of nights + 10% tax</strong>. Use the <strong>Print Bill</strong> button to produce a physical receipt for the guest.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">🛏️</div>
            <h3>7. Room Types & Rates</h3>
            <p>
                <strong>Standard:</strong> LKR 5,500/night – Garden view<br>
                <strong>Deluxe:</strong> LKR 8,500/night – Partial sea view<br>
                <strong>Sea View:</strong> LKR 12,000/night – Full ocean view<br>
                <strong>Suite:</strong> LKR 20,000/night – Luxury suite
            </p>
        </div>

        <div class="help-card">
            <div class="help-icon">⚠️</div>
            <h3>8. Common Errors & Solutions</h3>
            <p>
                <strong>Login failed:</strong> Check username/password carefully.<br>
                <strong>Invalid date:</strong> Check-in must be today or future. Check-out must be after check-in.<br>
                <strong>Invalid phone:</strong> Use digits, +, spaces, or dashes only (7–15 chars).<br>
                <strong>DB error:</strong> Contact system admin if database errors appear.
            </p>
        </div>

        <div class="help-card">
            <div class="help-icon">🔒</div>
            <h3>9. Security & Privacy</h3>
            <p>Never share your login credentials. Always <strong>logout</strong> after finishing your session, especially on shared computers. Guest data is stored securely in a MySQL database. Do not share reservation details with unauthorized parties.</p>
        </div>

        <div class="help-card">
            <div class="help-icon">📞</div>
            <h3>10. Contact & Support</h3>
            <p>
                <strong>System Admin:</strong> admin@oceanviewresort.lk<br>
                <strong>Module Leader:</strong> priyanga@icbtcampus.edu.lk<br>
                <strong>Hotel Front Desk:</strong> +94 91 222 3456<br>
                <strong>Location:</strong> Galle, Southern Province, Sri Lanka
            </p>
        </div>
    </div>
</div>
</body>
</html>
