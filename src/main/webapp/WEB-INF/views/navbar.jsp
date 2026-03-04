<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard" class="navbar-brand">
        <div class="brand-icon">🏨</div>
        <div class="brand-text">
            <div class="title">Ocean View Resort</div>
            <div class="subtitle">Reservation System</div>
        </div>
    </a>

    <div class="navbar-nav">
        <a href="${pageContext.request.contextPath}/dashboard"    class="nav-link">🏠 Dashboard</a>
        <a href="${pageContext.request.contextPath}/reservations" class="nav-link">📋 Reservations</a>
        <a href="${pageContext.request.contextPath}/add-reservation" class="nav-link">➕ New Booking</a>
        <a href="${pageContext.request.contextPath}/help"         class="nav-link">❓ Help</a>
    </div>

    <div class="navbar-right">
        <div class="user-badge">
            <div class="user-avatar-sm">${fn:substring(sessionScope.fullName, 0, 1)}</div>
            <span>${sessionScope.fullName}</span>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⏻ Logout</a>
    </div>
</nav>
