package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reservation Model - Represents a guest reservation at Ocean View Resort.
 * Encapsulates all guest and booking data.
 */
public class Reservation {

    private int id;
    private String reservationNumber;
    private String guestName;
    private String address;
    private String contactNumber;
    private String email;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numGuests;
    private String specialRequests;
    private String status;
    private BigDecimal totalAmount;
    private String createdBy;
    private String createdAt;

    public Reservation() {}

    // ==================== Computed Properties ====================

    /**
     * Calculates number of nights between check-in and check-out.
     */
    public long getNumNights() {
        if (checkInDate != null && checkOutDate != null) {
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }

    /**
     * Returns initials for avatar display.
     */
    public String getInitials() {
        if (guestName == null || guestName.trim().isEmpty()) return "?";
        String[] parts = guestName.trim().split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        }
        return guestName.substring(0, Math.min(2, guestName.length())).toUpperCase();
    }

    /**
     * Returns status badge CSS class.
     */
    public String getStatusClass() {
        if (status == null)              return "badge-info";
        if ("CONFIRMED".equals(status))  return "badge-primary";
        if ("CHECKED_IN".equals(status)) return "badge-success";
        if ("CHECKED_OUT".equals(status))return "badge-secondary";
        if ("CANCELLED".equals(status))  return "badge-danger";
        return "badge-info";
    }

    // ==================== Getters & Setters ====================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReservationNumber() { return reservationNumber; }
    public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getNumGuests() { return numGuests; }
    public void setNumGuests(int numGuests) { this.numGuests = numGuests; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
