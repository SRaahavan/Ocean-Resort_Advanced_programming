<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Reservations – Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<c:if test="${not empty successMsg}">
    <div id="successDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">✅</span>
            <h3>Done!</h3>
            <p>${successMsg}</p>
            <button class="dialog-btn success-btn" onclick="document.getElementById('successDialog').style.display='none'">OK</button>
        </div>
    </div>
</c:if>

<c:if test="${not empty errorMsg}">
    <div id="errorDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">❌</span>
            <h3>Error</h3>
            <p>${errorMsg}</p>
            <button class="dialog-btn error-btn" onclick="document.getElementById('errorDialog').style.display='none'">OK</button>
        </div>
    </div>
</c:if>

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📋 All Reservations</h1>
            <p class="page-subtitle">Total: ${fn:length(reservations)} reservation(s) found</p>
        </div>
        <div style="display:flex; gap:10px; align-items:center;">
            <!-- Search -->
            <form method="get" action="${pageContext.request.contextPath}/reservations" class="search-bar">
                <input type="text" name="search" class="search-input"
                       placeholder="Search guest name or reservation no..."
                       value="${not empty searchKeyword ? searchKeyword : ''}">
                <button type="submit" class="btn btn-primary">🔍 Search</button>
                <c:if test="${not empty searchKeyword}">
                    <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">✕ Clear</a>
                </c:if>
            </form>
            <a href="${pageContext.request.contextPath}/add-reservation" class="btn btn-success">➕ New</a>
        </div>
    </div>

    <c:if test="${not empty dbError}">
        <div class="alert alert-danger">⚠️ ${dbError}</div>
    </c:if>

    <div class="table-container">
        <c:choose>
            <c:when test="${empty reservations}">
                <div class="empty-state">
                    <span class="empty-icon">📭</span>
                    <h3>No reservations found</h3>
                    <p><c:choose>
                        <c:when test="${not empty searchKeyword}">No results for "<strong>${searchKeyword}</strong>".</c:when>
                        <c:otherwise>Start by adding a new reservation.</c:otherwise>
                    </c:choose></p>
                    <a href="${pageContext.request.contextPath}/add-reservation" class="btn btn-primary">➕ Add Reservation</a>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Res. No.</th>
                            <th>Guest</th>
                            <th>Room</th>
                            <th>Check-In</th>
                            <th>Check-Out</th>
                            <th>Nights</th>
                            <th>Status</th>
                            <th>Total (LKR)</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="r" items="${reservations}">
                        <tr>
                            <td><code style="font-size:0.85rem;">${r.reservationNumber}</code></td>
                            <td>
                                <div class="guest-cell">
                                    <div class="guest-avatar">${r.initials}</div>
                                    <div>
                                        <div class="guest-name">${r.guestName}</div>
                                        <div class="guest-contact">📞 ${r.contactNumber}</div>
                                    </div>
                                </div>
                            </td>
                            <td><span class="badge badge-info">${r.roomType}</span></td>
                            <td>${r.checkInDate}</td>
                            <td>${r.checkOutDate}</td>
                            <td style="text-align:center;">${r.numNights}</td>
                            <td><span class="badge ${r.statusClass}">${r.status}</span></td>
                            <td style="font-weight:600; color:#1b2d42;">${r.totalAmount}</td>
                            <td>
                                <div style="display:flex; gap:5px; flex-wrap:wrap;">
                                    <a href="${pageContext.request.contextPath}/view-reservation?resNum=${r.reservationNumber}"
                                       class="btn btn-primary btn-sm">👁 View</a>
                                    <a href="${pageContext.request.contextPath}/bill?resNum=${r.reservationNumber}"
                                       class="btn btn-warning btn-sm">🧾 Bill</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
