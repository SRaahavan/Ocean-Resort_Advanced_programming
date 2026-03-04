package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;
import com.oceanview.util.DBConnectionPool;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ReservationDAO - Data Access Object for the Reservations table.
 * 
 * Design Pattern: DAO Pattern (Data Access Object)
 * Purpose: Separates database logic from business logic (3-tier architecture).
 */
public class ReservationDAO {

    /**
     * Generates a unique reservation number like RES-20250001
     */
    public String generateReservationNumber() throws SQLException {
        String year = String.valueOf(LocalDate.now().getYear());
        String sql = "SELECT COUNT(*) FROM reservations WHERE reservation_number LIKE ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "RES-" + year + "%");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1) + 1;
            return String.format("RES-%s%04d", year, count);
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
    }

    /**
     * Inserts a new reservation into the database.
     */
    public boolean addReservation(Reservation r) throws SQLException {
        String sql = "INSERT INTO reservations " +
            "(reservation_number, guest_name, address, contact_number, email," +
            " room_type, check_in_date, check_out_date, num_guests, special_requests," +
            " status, total_amount, created_by)" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReservationNumber());
            ps.setString(2, r.getGuestName());
            ps.setString(3, r.getAddress());
            ps.setString(4, r.getContactNumber());
            ps.setString(5, r.getEmail());
            ps.setString(6, r.getRoomType());
            ps.setDate(7, Date.valueOf(r.getCheckInDate()));
            ps.setDate(8, Date.valueOf(r.getCheckOutDate()));
            ps.setInt(9, r.getNumGuests());
            ps.setString(10, r.getSpecialRequests());
            ps.setString(11, "CONFIRMED");
            ps.setBigDecimal(12, r.getTotalAmount());
            ps.setString(13, r.getCreatedBy());
            return ps.executeUpdate() > 0;
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
    }

    /**
     * Retrieves a reservation by reservation number.
     */
    public Reservation getByReservationNumber(String resNum) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE reservation_number = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapReservation(rs);
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return null;
    }

    /**
     * Retrieves all reservations, ordered by creation date descending.
     */
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations ORDER BY created_at DESC";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapReservation(rs));
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return list;
    }

    /**
     * Searches reservations by guest name or reservation number (keyword).
     */
    public List<Reservation> searchReservations(String keyword) throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations" +
            " WHERE guest_name LIKE ? OR reservation_number LIKE ?" +
            " ORDER BY created_at DESC";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapReservation(rs));
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return list;
    }

    /**
     * Updates reservation status (e.g., CHECKED_IN, CHECKED_OUT, CANCELLED).
     */
    public boolean updateStatus(String resNum, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_number = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, resNum);
            return ps.executeUpdate() > 0;
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
    }

    /**
     * Counts total reservations.
     */
    public int countTotal() throws SQLException {
        return countByStatus(null);
    }

    public int countByStatus(String status) throws SQLException {
        String sql = (status == null)
            ? "SELECT COUNT(*) FROM reservations"
            : "SELECT COUNT(*) FROM reservations WHERE status = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null) ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
    }

    /**
     * Fetches today's check-ins.
     */
    public List<Reservation> getTodayCheckIns() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE check_in_date = CURDATE() ORDER BY guest_name";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapReservation(rs));
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return list;
    }

    // ==================== Room Types ====================

    /**
     * Retrieves all room types with rates.
     */
    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM room_types ORDER BY rate_per_night";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new RoomType(
                    rs.getInt("id"),
                    rs.getString("type_name"),
                    rs.getBigDecimal("rate_per_night"),
                    rs.getString("description")
                ));
            }
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return list;
    }

    public RoomType getRoomTypeByName(String name) throws SQLException {
        String sql = "SELECT * FROM room_types WHERE type_name = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RoomType(
                    rs.getInt("id"),
                    rs.getString("type_name"),
                    rs.getBigDecimal("rate_per_night"),
                    rs.getString("description")
                );
            }
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return null;
    }

    // ==================== Admin ====================

    public boolean validateAdmin(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE username = ? AND password = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
    }

    public String getAdminFullName(String username) throws SQLException {
        String sql = "SELECT full_name FROM admin WHERE username = ?";
        Connection conn = DBConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("full_name");
        } finally {
            DBConnectionPool.getInstance().closeConnection(conn);
        }
        return username;
    }

    // ==================== Private Mapper ====================

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));
        r.setReservationNumber(rs.getString("reservation_number"));
        r.setGuestName(rs.getString("guest_name"));
        r.setAddress(rs.getString("address"));
        r.setContactNumber(rs.getString("contact_number"));
        r.setEmail(rs.getString("email"));
        r.setRoomType(rs.getString("room_type"));
        Date checkIn = rs.getDate("check_in_date");
        if (checkIn != null) r.setCheckInDate(checkIn.toLocalDate());
        Date checkOut = rs.getDate("check_out_date");
        if (checkOut != null) r.setCheckOutDate(checkOut.toLocalDate());
        r.setNumGuests(rs.getInt("num_guests"));
        r.setSpecialRequests(rs.getString("special_requests"));
        r.setStatus(rs.getString("status"));
        r.setTotalAmount(rs.getBigDecimal("total_amount"));
        r.setCreatedBy(rs.getString("created_by"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toString().substring(0, 16));
        return r;
    }
}
