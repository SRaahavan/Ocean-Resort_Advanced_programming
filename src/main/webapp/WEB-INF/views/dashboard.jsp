<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<!-- Success/Error Dialogs from session flash -->
<c:if test="${not empty sessionScope.successMsg}">
    <div id="successDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">✅</span>
            <h3>Success!</h3>
            <p>${sessionScope.successMsg}</p>
            <button class="dialog-btn success-btn" onclick="document.getElementById('successDialog').style.display='none'">Great!</button>
        </div>
    </div>
    <% session.removeAttribute("successMsg"); %>
</c:if>

<c:if test="${not empty sessionScope.errorMsg}">
    <div id="errorDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">❌</span>
            <h3>Error</h3>
            <p>${sessionScope.errorMsg}</p>
            <button class="dialog-btn error-btn" onclick="document.getElementById('errorDialog').style.display='none'">OK</button>
        </div>
    </div>
    <% session.removeAttribute("errorMsg"); %>
</c:if>

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🏠 Dashboard</h1>
            <p class="page-subtitle">Welcome back, ${sessionScope.fullName} — here's today's overview</p>
        </div>
        <a href="${pageContext.request.contextPath}/add-reservation" class="btn btn-success">
            ➕ New Reservation
        </a>
    </div>

    <c:if test="${not empty dbError}">
        <div class="alert alert-danger">⚠️ Database Error: ${dbError}</div>
    </c:if>

    <!-- Stats Cards -->
    <div class="stats-grid">
        <div class="stat-card total">
            <div class="stat-icon blue">📊</div>
            <div>
                <div class="stat-num">${stats.total}</div>
                <div class="stat-label">Total Reservations</div>
            </div>
        </div>
        <div class="stat-card confirmed">
            <div class="stat-icon gold">🔖</div>
            <div>
                <div class="stat-num">${stats.confirmed}</div>
                <div class="stat-label">Confirmed</div>
            </div>
        </div>
        <div class="stat-card checkin">
            <div class="stat-icon green">🛎️</div>
            <div>
                <div class="stat-num">${stats.checkedIn}</div>
                <div class="stat-label">Checked In</div>
            </div>
        </div>
        <div class="stat-card checkout">
            <div class="stat-icon gray">🚪</div>
            <div>
                <div class="stat-num">${stats.checkedOut}</div>
                <div class="stat-label">Checked Out</div>
            </div>
        </div>
        <div class="stat-card cancelled">
            <div class="stat-icon red">❌</div>
            <div>
                <div class="stat-num">${stats.cancelled}</div>
                <div class="stat-label">Cancelled</div>
            </div>
        </div>
    </div>

    <!-- Today's Check-Ins -->
    <div class="card" style="margin-bottom:24px;">
        <div class="card-header">
            <h3>🌅 Today's Check-Ins</h3>
            <span class="badge badge-success">${fn:length(todayCheckIns)} guests</span>
        </div>
        <c:choose>
            <c:when test="${empty todayCheckIns}">
                <div class="empty-state" style="padding:30px;">
                    <span style="font-size:2rem;">🌴</span>
                    <p style="margin-top:10px; color:#bbb;">No check-ins scheduled for today.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Reservation No.</th>
                                <th>Guest Name</th>
                                <th>Room Type</th>
                                <th>Contact</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${todayCheckIns}">
                            <tr>
                                <td><code>${r.reservationNumber}</code></td>
                                <td>
                                    <div class="guest-cell">
                                        <div class="guest-avatar">${r.initials}</div>
                                        <div class="guest-name">${r.guestName}</div>
                                    </div>
                                </td>
                                <td><span class="badge badge-info">${r.roomType}</span></td>
                                <td>${r.contactNumber}</td>
                                <td><span class="badge ${r.statusClass}">${r.status}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/view-reservation?resNum=${r.reservationNumber}"
                                       class="btn btn-primary btn-sm">View</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Recent Reservations -->
    <div class="card">
        <div class="card-header">
            <h3>📋 Recent Reservations</h3>
            <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary btn-sm">View All →</a>
        </div>
        <c:choose>
            <c:when test="${empty recentReservations}">
                <div class="empty-state">
                    <span class="empty-icon">📭</span>
                    <h3>No reservations yet</h3>
                    <p>Click "New Reservation" to make the first booking.</p>
                    <a href="${pageContext.request.contextPath}/add-reservation" class="btn btn-primary">➕ Add First Reservation</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Res. No.</th>
                                <th>Guest</th>
                                <th>Room Type</th>
                                <th>Check-In</th>
                                <th>Check-Out</th>
                                <th>Status</th>
                                <th>Total (LKR)</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${recentReservations}" begin="0" end="9">
                            <tr>
                                <td><code>${r.reservationNumber}</code></td>
                                <td>
                                    <div class="guest-cell">
                                        <div class="guest-avatar">${r.initials}</div>
                                        <div>
                                            <div class="guest-name">${r.guestName}</div>
                                            <div class="guest-contact">${r.contactNumber}</div>
                                        </div>
                                    </div>
                                </td>
                                <td><span class="badge badge-info">${r.roomType}</span></td>
                                <td>${r.checkInDate}</td>
                                <td>${r.checkOutDate}</td>
                                <td><span class="badge ${r.statusClass}">${r.status}</span></td>
                                <td style="font-weight:600;">${r.totalAmount}</td>
                                <td style="display:flex; gap:6px;">
                                    <a href="${pageContext.request.contextPath}/view-reservation?resNum=${r.reservationNumber}"
                                       class="btn btn-primary btn-sm">View</a>
                                    <a href="${pageContext.request.contextPath}/bill?resNum=${r.reservationNumber}"
                                       class="btn btn-warning btn-sm">Bill</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
