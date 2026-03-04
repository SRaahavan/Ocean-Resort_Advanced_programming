<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${reservation.guestName} – Reservation Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<c:if test="${not empty sessionScope.successMsg}">
    <div id="successDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">✅</span>
            <h3>Updated!</h3>
            <p>${sessionScope.successMsg}</p>
            <button class="dialog-btn success-btn" onclick="document.getElementById('successDialog').style.display='none'">OK</button>
        </div>
    </div>
    <% session.removeAttribute("successMsg"); %>
</c:if>

<div class="container">
    <a href="${pageContext.request.contextPath}/reservations" class="back-link">← Back to Reservations</a>

    <div class="detail-card">
        <!-- Header -->
        <div class="detail-header">
            <div class="detail-avatar">${reservation.initials}</div>
            <div class="detail-header-info">
                <h2>${reservation.guestName}</h2>
                <div class="res-num">🔖 ${reservation.reservationNumber}</div>
                <div style="margin-top:8px;">
                    <span class="badge ${reservation.statusClass}">${reservation.status}</span>
                    <span class="badge badge-info" style="margin-left:6px;">${reservation.roomType}</span>
                </div>
            </div>
        </div>

        <div class="detail-body">
            <!-- Guest Details -->
            <h3 style="font-size:0.85rem; color:#7986cb; text-transform:uppercase; letter-spacing:1px; margin-bottom:14px;">👤 Guest Details</h3>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">📞 Contact Number</div>
                    <div class="info-value">${reservation.contactNumber}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">📧 Email</div>
                    <div class="info-value">${not empty reservation.email ? reservation.email : '—'}</div>
                </div>
                <div class="info-item full">
                    <div class="info-label">📍 Address</div>
                    <div class="info-value">${reservation.address}</div>
                </div>
            </div>

            <!-- Booking Details -->
            <h3 style="font-size:0.85rem; color:#7986cb; text-transform:uppercase; letter-spacing:1px; margin:20px 0 14px;">🏨 Booking Details</h3>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">🛏 Room Type</div>
                    <div class="info-value">${reservation.roomType}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">👥 Guests</div>
                    <div class="info-value">${reservation.numGuests}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">📅 Check-In</div>
                    <div class="info-value">${reservation.checkInDate}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">📅 Check-Out</div>
                    <div class="info-value">${reservation.checkOutDate}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">🌙 Duration</div>
                    <div class="info-value">${reservation.numNights} Night(s)</div>
                </div>
                <div class="info-item">
                    <div class="info-label">💰 Total Amount</div>
                    <div class="info-value" style="font-size:1.1rem; color:#1565c0; font-weight:700;">LKR ${reservation.totalAmount}</div>
                </div>
                <c:if test="${not empty reservation.specialRequests}">
                    <div class="info-item full">
                        <div class="info-label">📝 Special Requests</div>
                        <div class="info-value">${reservation.specialRequests}</div>
                    </div>
                </c:if>
            </div>

            <!-- System Info -->
            <h3 style="font-size:0.85rem; color:#7986cb; text-transform:uppercase; letter-spacing:1px; margin:20px 0 14px;">ℹ️ System Information</h3>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">🕒 Created At</div>
                    <div class="info-value">${reservation.createdAt}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">👤 Created By</div>
                    <div class="info-value">${reservation.createdBy}</div>
                </div>
            </div>

            <!-- Status Update + Actions -->
            <div style="margin-top:24px; padding-top:20px; border-top:1px solid #f0f0f0; display:flex; gap:12px; flex-wrap:wrap; align-items:center;">
                <!-- Update Status -->
                <form method="post" action="${pageContext.request.contextPath}/update-status"
                      style="display:flex; gap:8px; align-items:center;">
                    <input type="hidden" name="resNum" value="${reservation.reservationNumber}">
                    <input type="hidden" name="redirectTo" value="view">
                    <select name="status" style="padding:10px 14px; border:2px solid #e0e0e0; border-radius:8px; font-size:0.88rem;">
                        <option value="CONFIRMED"   ${reservation.status=='CONFIRMED'   ? 'selected':''}>Confirmed</option>
                        <option value="CHECKED_IN"  ${reservation.status=='CHECKED_IN'  ? 'selected':''}>Checked In</option>
                        <option value="CHECKED_OUT" ${reservation.status=='CHECKED_OUT' ? 'selected':''}>Checked Out</option>
                        <option value="CANCELLED"   ${reservation.status=='CANCELLED'   ? 'selected':''}>Cancelled</option>
                    </select>
                    <button type="submit" class="btn btn-primary btn-sm">Update Status</button>
                </form>

                <a href="${pageContext.request.contextPath}/bill?resNum=${reservation.reservationNumber}"
                   class="btn btn-warning">🧾 View Bill</a>
                <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">← Back</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
