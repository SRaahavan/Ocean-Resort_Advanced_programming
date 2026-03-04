<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Reservation – Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<!-- Validation Error Dialog -->
<c:if test="${not empty error}">
    <div id="errorDialog" class="dialog-overlay">
        <div class="dialog-box">
            <span class="dialog-icon">⚠️</span>
            <h3>Validation Error</h3>
            <p>${error}</p>
            <button class="dialog-btn error-btn" onclick="document.getElementById('errorDialog').style.display='none'">Fix It</button>
        </div>
    </div>
</c:if>

<div class="container">
    <a href="${pageContext.request.contextPath}/dashboard" class="back-link">← Back to Dashboard</a>

    <div class="form-card">
        <h2 class="page-title" style="margin-bottom:4px;">➕ New Reservation</h2>
        <p class="page-subtitle" style="margin-bottom:0;">Register a new guest booking at Ocean View Resort</p>

        <form method="post" action="${pageContext.request.contextPath}/add-reservation"
              onsubmit="return validateReservationForm()">

            <!-- Guest Information -->
            <div class="form-section-title">👤 Guest Information</div>

            <div class="form-group">
                <label>Full Name *</label>
                <input type="text" name="guestName" placeholder="e.g. Kamal Perera"
                       value="${not empty formData.guestName ? formData.guestName : ''}"
                       maxlength="100" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Contact Number *</label>
                    <input type="tel" name="contactNumber" placeholder="e.g. +94 71 234 5678"
                           value="${not empty formData.contactNumber ? formData.contactNumber : ''}"
                           maxlength="20" required>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" placeholder="guest@email.com"
                           value="${not empty formData.email ? formData.email : ''}">
                </div>
            </div>

            <div class="form-group">
                <label>Address *</label>
                <textarea name="address" placeholder="Full residential address..." maxlength="300" required>${not empty formData.address ? formData.address : ''}</textarea>
            </div>

            <!-- Booking Details -->
            <div class="form-section-title">🛏️ Booking Details</div>

            <div class="form-row">
                <div class="form-group">
                    <label>Room Type *</label>
                    <select name="roomType" required onchange="updateRate(this)">
                        <option value="">-- Select Room Type --</option>
                        <c:forEach var="rt" items="${roomTypes}">
                            <option value="${rt.typeName}"
                                    data-rate="${rt.ratePerNight}"
                                    <c:if test="${formData.roomType == rt.typeName}">selected</c:if>>
                                ${rt.typeName} – LKR ${rt.ratePerNight}/night
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Number of Guests *</label>
                    <select name="numGuests">
                        <c:forEach begin="1" end="6" var="n">
                            <option value="${n}" <c:if test="${formData.numGuests == n}">selected</c:if>>${n} Guest${n > 1 ? 's' : ''}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Check-In Date *</label>
                    <input type="date" name="checkInDate" id="checkInDate"
                           value="${not empty formData.checkInDate ? formData.checkInDate : ''}"
                           required onchange="computeCost()">
                </div>
                <div class="form-group">
                    <label>Check-Out Date *</label>
                    <input type="date" name="checkOutDate" id="checkOutDate"
                           value="${not empty formData.checkOutDate ? formData.checkOutDate : ''}"
                           required onchange="computeCost()">
                </div>
            </div>

            <!-- Live Cost Preview -->
            <div id="costPreview" style="display:none; background:#e8f5e9; border:1px solid #a5d6a7; border-radius:10px; padding:14px 18px; margin-bottom:16px; font-size:0.92rem;">
                🏷️ Estimated Cost: <strong id="costText"></strong> (excluding 10% tax)
            </div>

            <div class="form-group">
                <label>Special Requests</label>
                <textarea name="specialRequests" placeholder="Any special requests or notes...">${not empty formData.specialRequests ? formData.specialRequests : ''}</textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">💾 Confirm Reservation</button>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script>
// Set minimum date to today
document.addEventListener('DOMContentLoaded', function() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('checkInDate').min = today;
    document.getElementById('checkOutDate').min = today;
});

let currentRate = 0;

function updateRate(sel) {
    const opt = sel.options[sel.selectedIndex];
    currentRate = parseFloat(opt.dataset.rate) || 0;
    computeCost();
}

function computeCost() {
    const ci = document.getElementById('checkInDate').value;
    const co = document.getElementById('checkOutDate').value;
    if (ci && co && currentRate > 0) {
        const nights = Math.round((new Date(co) - new Date(ci)) / 86400000);
        if (nights > 0) {
            const cost = (nights * currentRate).toLocaleString('en-LK', {minimumFractionDigits:2});
            document.getElementById('costText').textContent = `LKR ${cost} (${nights} night${nights>1?'s':''} × LKR ${currentRate.toLocaleString()})`;
            document.getElementById('costPreview').style.display = 'block';
        } else {
            document.getElementById('costPreview').style.display = 'none';
        }
    }
}

function validateReservationForm() {
    const name    = document.querySelector('[name=guestName]').value.trim();
    const phone   = document.querySelector('[name=contactNumber]').value.trim();
    const address = document.querySelector('[name=address]').value.trim();
    const room    = document.querySelector('[name=roomType]').value;
    const ci      = document.getElementById('checkInDate').value;
    const co      = document.getElementById('checkOutDate').value;

    if (!name) return showErr('Guest name is required.');
    if (name.length < 2) return showErr('Guest name must be at least 2 characters.');
    if (!phone) return showErr('Contact number is required.');
    if (!/^[+]?[0-9\s\-]{7,15}$/.test(phone)) return showErr('Invalid contact number format. Use digits, spaces, dashes, or a leading +.');
    if (!address) return showErr('Address is required.');
    if (!room) return showErr('Please select a room type.');
    if (!ci) return showErr('Check-in date is required.');
    if (!co) return showErr('Check-out date is required.');

    const today = new Date(); today.setHours(0,0,0,0);
    if (new Date(ci) < today) return showErr('Check-in date cannot be in the past.');
    if (new Date(co) <= new Date(ci)) return showErr('Check-out date must be after check-in date.');

    return true;
}

function showErr(msg) {
    const overlay = document.createElement('div');
    overlay.className = 'dialog-overlay';
    overlay.id = 'clientErr';
    overlay.innerHTML = `
        <div class="dialog-box">
            <span class="dialog-icon">⚠️</span>
            <h3>Validation Error</h3>
            <p>${msg}</p>
            <button class="dialog-btn error-btn" onclick="document.getElementById('clientErr').remove()">Fix It</button>
        </div>`;
    document.body.appendChild(overlay);
    return false;
}
</script>
</body>
</html>
