<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill – ${bill.reservation.reservationNumber}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        @media print {
            .navbar, .back-link, .print-btn, .no-print { display: none !important; }
            body { background: white; }
            .bill-card { box-shadow: none; max-width: 100%; }
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="container">
    <a href="${pageContext.request.contextPath}/view-reservation?resNum=${bill.reservation.reservationNumber}"
       class="back-link">← Back to Reservation</a>

    <div class="bill-card">
        <div class="bill-header">
            <div style="font-size:2.5rem; margin-bottom:10px;">🏨</div>
            <h2>Ocean View Resort</h2>
            <p>Galle, Sri Lanka &nbsp;|&nbsp; +94 91 222 3456 &nbsp;|&nbsp; info@oceanviewresort.lk</p>
        </div>

        <div class="bill-body">
            <!-- Title -->
            <div style="text-align:center; margin-bottom:24px; padding-bottom:20px; border-bottom:2px dashed #e0e0e0;">
                <h3 style="font-family:'Playfair Display',serif; font-size:1.3rem; color:#0d1b2a;">INVOICE / BILL</h3>
                <p style="color:#888; font-size:0.85rem; margin-top:5px;">Reservation No: <strong>${bill.reservation.reservationNumber}</strong></p>
            </div>

            <!-- Guest Info -->
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:12px; margin-bottom:20px; font-size:0.9rem;">
                <div>
                    <p style="color:#888; font-size:0.78rem; text-transform:uppercase; margin-bottom:3px;">GUEST NAME</p>
                    <p style="font-weight:600;">${bill.reservation.guestName}</p>
                </div>
                <div>
                    <p style="color:#888; font-size:0.78rem; text-transform:uppercase; margin-bottom:3px;">CONTACT</p>
                    <p style="font-weight:600;">${bill.reservation.contactNumber}</p>
                </div>
                <div>
                    <p style="color:#888; font-size:0.78rem; text-transform:uppercase; margin-bottom:3px;">CHECK-IN DATE</p>
                    <p style="font-weight:600;">${bill.reservation.checkInDate}</p>
                </div>
                <div>
                    <p style="color:#888; font-size:0.78rem; text-transform:uppercase; margin-bottom:3px;">CHECK-OUT DATE</p>
                    <p style="font-weight:600;">${bill.reservation.checkOutDate}</p>
                </div>
            </div>

            <!-- Bill Breakdown -->
            <div style="background:#f8f9fa; border-radius:10px; padding:18px; margin-bottom:20px;">
                <div class="bill-row">
                    <span class="bill-label">Room Type</span>
                    <span class="bill-value">${bill.reservation.roomType}</span>
                </div>
                <div class="bill-row">
                    <span class="bill-label">Rate per Night</span>
                    <span class="bill-value">LKR ${bill.ratePerNight}</span>
                </div>
                <div class="bill-row">
                    <span class="bill-label">Number of Nights</span>
                    <span class="bill-value">${bill.numNights} Night(s)</span>
                </div>
                <div class="bill-row">
                    <span class="bill-label">Subtotal (${bill.numNights} × LKR ${bill.ratePerNight})</span>
                    <span class="bill-value">LKR ${bill.subtotal}</span>
                </div>
                <div class="bill-row">
                    <span class="bill-label">Tax (10%)</span>
                    <span class="bill-value">LKR ${bill.tax}</span>
                </div>
            </div>

            <div class="bill-total-row">
                <span class="bill-total-label">TOTAL AMOUNT DUE</span>
                <span class="bill-total-value">LKR ${bill.total}</span>
            </div>

            <!-- Status -->
            <div style="text-align:center; margin-top:20px;">
                <span class="badge ${bill.reservation.statusClass}" style="font-size:0.85rem; padding:8px 18px;">
                    Status: ${bill.reservation.status}
                </span>
            </div>

            <!-- Footer -->
            <div style="text-align:center; margin-top:24px; padding-top:16px; border-top:1px dashed #e0e0e0; font-size:0.8rem; color:#bbb;">
                <p>Thank you for choosing Ocean View Resort! We hope you had a wonderful stay.</p>
                <p style="margin-top:5px;">Bill generated on: <%= new java.util.Date() %></p>
            </div>
        </div>
    </div>

    <div style="text-align:center; margin-top:20px;" class="no-print">
        <button class="btn btn-primary print-btn" onclick="window.print()">🖨️ Print Bill</button>
        <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary" style="margin-left:10px;">← Back</a>
    </div>
</div>
</body>
</html>
