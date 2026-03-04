package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ReservationService - Business Logic / Service Layer.
 * 
 * Design Pattern: Facade Pattern
 * Purpose: Provides a simplified interface to the DAO layer.
 *          Handles all business rules (validation, billing calculation, etc.)
 *          This is the middle tier in the 3-tier architecture.
 */
public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    // Constructor injection for testability
    public ReservationService(ReservationDAO dao) {
        this.reservationDAO = dao;
    }

    // ==================== Authentication ====================

    public boolean login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username is required.");
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password is required.");
        return reservationDAO.validateAdmin(username.trim(), password.trim());
    }

    public String getStaffFullName(String username) throws Exception {
        return reservationDAO.getAdminFullName(username);
    }

    // ==================== Reservations ====================

    /**
     * Creates a new reservation after full validation.
     * Implements business rules:
     * - check-in must be today or future
     * - check-out must be after check-in
     * - all required fields must be present
     * - auto-generates reservation number
     * - calculates total bill
     */
    public Reservation createReservation(Map<String, String> params, String createdBy) throws Exception {
        validateReservationInput(params);

        String guestName     = params.get("guestName").trim();
        String address       = params.get("address").trim();
        String contactNumber = params.get("contactNumber").trim();
        String email         = params.get("email") != null ? params.get("email").trim() : "";
        String roomType      = params.get("roomType").trim();
        String checkInStr    = params.get("checkInDate").trim();
        String checkOutStr   = params.get("checkOutDate").trim();
        int numGuests        = Integer.parseInt(params.getOrDefault("numGuests", "1"));
        String special       = params.getOrDefault("specialRequests", "");

        LocalDate checkIn  = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past.");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        RoomType rt = reservationDAO.getRoomTypeByName(roomType);
        if (rt == null) {
            throw new IllegalArgumentException("Invalid room type selected.");
        }

        long numNights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal total = rt.getRatePerNight().multiply(BigDecimal.valueOf(numNights));

        Reservation r = new Reservation();
        r.setReservationNumber(reservationDAO.generateReservationNumber());
        r.setGuestName(guestName);
        r.setAddress(address);
        r.setContactNumber(contactNumber);
        r.setEmail(email);
        r.setRoomType(roomType);
        r.setCheckInDate(checkIn);
        r.setCheckOutDate(checkOut);
        r.setNumGuests(numGuests);
        r.setSpecialRequests(special);
        r.setTotalAmount(total);
        r.setStatus("CONFIRMED");
        r.setCreatedBy(createdBy);

        boolean saved = reservationDAO.addReservation(r);
        if (!saved) throw new Exception("Failed to save reservation. Please try again.");

        return r;
    }

    public Reservation getReservation(String resNum) throws Exception {
        if (resNum == null || resNum.trim().isEmpty())
            throw new IllegalArgumentException("Reservation number is required.");
        return reservationDAO.getByReservationNumber(resNum.trim());
    }

    public List<Reservation> getAllReservations() throws Exception {
        return reservationDAO.getAllReservations();
    }

    public List<Reservation> searchReservations(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty())
            return getAllReservations();
        return reservationDAO.searchReservations(keyword.trim());
    }

    public void updateStatus(String resNum, String status) throws Exception {
        List<String> valid = List.of("CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED");
        if (!valid.contains(status))
            throw new IllegalArgumentException("Invalid status: " + status);
        boolean updated = reservationDAO.updateStatus(resNum, status);
        if (!updated) throw new Exception("Could not update status. Reservation not found.");
    }

    public List<RoomType> getRoomTypes() throws Exception {
        return reservationDAO.getAllRoomTypes();
    }

    public List<Reservation> getTodayCheckIns() throws Exception {
        return reservationDAO.getTodayCheckIns();
    }

    // ==================== Dashboard Stats ====================

    public Map<String, Integer> getDashboardStats() throws Exception {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total",      reservationDAO.countTotal());
        stats.put("confirmed",  reservationDAO.countByStatus("CONFIRMED"));
        stats.put("checkedIn",  reservationDAO.countByStatus("CHECKED_IN"));
        stats.put("checkedOut", reservationDAO.countByStatus("CHECKED_OUT"));
        stats.put("cancelled",  reservationDAO.countByStatus("CANCELLED"));
        return stats;
    }

    // ==================== Bill Calculation ====================

    /**
     * Calculates detailed billing information for a reservation.
     */
    public Map<String, Object> calculateBill(Reservation r) throws Exception {
        RoomType rt = reservationDAO.getRoomTypeByName(r.getRoomType());
        if (rt == null) throw new Exception("Room type not found.");

        long nights = r.getNumNights();
        BigDecimal rate = rt.getRatePerNight();
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(nights));
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.10")); // 10% tax
        BigDecimal total = subtotal.add(tax);

        Map<String, Object> bill = new HashMap<>();
        bill.put("reservation", r);
        bill.put("roomType", rt);
        bill.put("numNights", nights);
        bill.put("ratePerNight", rate);
        bill.put("subtotal", subtotal);
        bill.put("tax", tax);
        bill.put("total", total);
        return bill;
    }

    // ==================== Validation ====================

    private void validateReservationInput(Map<String, String> p) {
        assertRequired(p.get("guestName"),     "Guest name");
        assertRequired(p.get("address"),       "Address");
        assertRequired(p.get("contactNumber"), "Contact number");
        assertRequired(p.get("roomType"),      "Room type");
        assertRequired(p.get("checkInDate"),   "Check-in date");
        assertRequired(p.get("checkOutDate"),  "Check-out date");

        // Phone format: digits, spaces, +, -
        String phone = p.get("contactNumber").trim();
        if (!phone.matches("^[+]?[0-9\\s\\-]{7,15}$")) {
            throw new IllegalArgumentException("Invalid contact number format.");
        }

        // Email format (optional)
        String email = p.get("email");
        if (email != null && !email.trim().isEmpty() && !email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address format.");
        }

        // Guest name length
        if (p.get("guestName").trim().length() < 2) {
            throw new IllegalArgumentException("Guest name must be at least 2 characters.");
        }

        // Num guests
        String ng = p.getOrDefault("numGuests", "1");
        try {
            int n = Integer.parseInt(ng);
            if (n < 1 || n > 10) throw new IllegalArgumentException("Number of guests must be between 1 and 10.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of guests.");
        }
    }

    private void assertRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }
}
