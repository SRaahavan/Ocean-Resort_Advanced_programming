package com.oceanview;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;
import com.oceanview.service.ReservationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ReservationServiceTest - Automated Unit Tests using JUnit 5 + Mockito.
 * 
 * Test-Driven Development (TDD) approach:
 * Tests are written to verify all business rules and validation logic
 * in the ReservationService before/alongside implementation.
 * 
 * Test Categories:
 * 1. Authentication Tests
 * 2. Reservation Validation Tests
 * 3. Business Rule Tests (date validation, billing)
 * 4. Positive / Happy Path Tests
 * 5. Edge Case Tests
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Ocean View Resort - Reservation System Tests")
class ReservationServiceTest {

    private ReservationDAO mockDao;
    private ReservationService service;

    @BeforeEach
    void setUp() throws Exception {
        mockDao = Mockito.mock(ReservationDAO.class);
        service = new ReservationService(mockDao);

        // Default mock: room type exists
        RoomType standard = new RoomType(1, "Standard", new BigDecimal("5500.00"), "Garden view");
        when(mockDao.getRoomTypeByName("Standard")).thenReturn(standard);
        when(mockDao.getRoomTypeByName("Deluxe")).thenReturn(
            new RoomType(2, "Deluxe", new BigDecimal("8500.00"), "Sea view"));
        when(mockDao.getRoomTypeByName("Suite")).thenReturn(
            new RoomType(4, "Suite", new BigDecimal("20000.00"), "Luxury suite"));
        when(mockDao.generateReservationNumber()).thenReturn("RES-20250001");
        when(mockDao.addReservation(any())).thenReturn(true);
        when(mockDao.validateAdmin("admin", "admin123")).thenReturn(true);
        when(mockDao.validateAdmin("staff", "staff123")).thenReturn(true);
        when(mockDao.validateAdmin(anyString(), eq("wrongpass"))).thenReturn(false);
    }

    // ============================================================
    // 1. AUTHENTICATION TESTS
    // ============================================================

    @Test
    @DisplayName("T01 - Login succeeds with valid admin credentials")
    void loginValidAdmin() throws Exception {
        assertTrue(service.login("admin", "admin123"),
            "Admin should be able to login with correct credentials");
    }

    @Test
    @DisplayName("T02 - Login succeeds with valid staff credentials")
    void loginValidStaff() throws Exception {
        assertTrue(service.login("staff", "staff123"),
            "Staff should be able to login with correct credentials");
    }

    @Test
    @DisplayName("T03 - Login fails with wrong password")
    void loginWrongPassword() throws Exception {
        assertFalse(service.login("admin", "wrongpass"),
            "Login should fail with wrong password");
    }

    @Test
    @DisplayName("T04 - Login throws exception when username is empty")
    void loginEmptyUsername() {
        assertThrows(IllegalArgumentException.class,
            () -> service.login("", "admin123"),
            "Should throw exception for empty username");
    }

    @Test
    @DisplayName("T05 - Login throws exception when password is empty")
    void loginEmptyPassword() {
        assertThrows(IllegalArgumentException.class,
            () -> service.login("admin", ""),
            "Should throw exception for empty password");
    }

    @Test
    @DisplayName("T06 - Login throws exception when username is null")
    void loginNullUsername() {
        assertThrows(IllegalArgumentException.class,
            () -> service.login(null, "admin123"),
            "Should throw exception for null username");
    }

    // ============================================================
    // 2. RESERVATION VALIDATION TESTS
    // ============================================================

    @Test
    @DisplayName("T07 - Reservation creation succeeds with valid data")
    void createReservationSuccess() throws Exception {
        Map<String, String> params = buildValidParams();
        Reservation r = service.createReservation(params, "admin");

        assertNotNull(r, "Reservation should not be null");
        assertEquals("Kamal Perera", r.getGuestName());
        assertEquals("RES-20250001", r.getReservationNumber());
        assertEquals("Standard", r.getRoomType());
        assertEquals("CONFIRMED", r.getStatus());
    }

    @Test
    @DisplayName("T08 - Throws exception when guest name is missing")
    void createReservationMissingName() {
        Map<String, String> params = buildValidParams();
        params.put("guestName", "");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should throw for empty guest name");
    }

    @Test
    @DisplayName("T09 - Throws exception when address is missing")
    void createReservationMissingAddress() {
        Map<String, String> params = buildValidParams();
        params.put("address", "");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should throw for empty address");
    }

    @Test
    @DisplayName("T10 - Throws exception when contact number is missing")
    void createReservationMissingPhone() {
        Map<String, String> params = buildValidParams();
        params.put("contactNumber", "");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should throw for empty contact number");
    }

    @Test
    @DisplayName("T11 - Throws exception when room type is missing")
    void createReservationMissingRoomType() {
        Map<String, String> params = buildValidParams();
        params.put("roomType", "");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should throw for empty room type");
    }

    // ============================================================
    // 3. PHONE NUMBER VALIDATION (Parameterized Tests)
    // ============================================================

    @ParameterizedTest(name = "T12 - Invalid phone: [{0}]")
    @ValueSource(strings = { "abc", "12", "!!##$$", "1234567890123456", "phone" })
    @DisplayName("T12 - Rejects invalid phone number formats")
    void invalidPhoneFormats(String phone) {
        Map<String, String> params = buildValidParams();
        params.put("contactNumber", phone);
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject invalid phone: " + phone);
    }

    @ParameterizedTest(name = "T13 - Valid phone: [{0}]")
    @ValueSource(strings = { "+94711234567", "0771234567", "+94 71 234 5678", "071-1234567" })
    @DisplayName("T13 - Accepts valid phone number formats")
    void validPhoneFormats(String phone) throws Exception {
        Map<String, String> params = buildValidParams();
        params.put("contactNumber", phone);
        Reservation r = service.createReservation(params, "admin");
        assertNotNull(r, "Should accept valid phone: " + phone);
    }

    // ============================================================
    // 4. DATE VALIDATION TESTS
    // ============================================================

    @Test
    @DisplayName("T14 - Throws exception when check-in is in the past")
    void checkInInPast() {
        Map<String, String> params = buildValidParams();
        params.put("checkInDate", LocalDate.now().minusDays(1).toString());
        params.put("checkOutDate", LocalDate.now().toString());
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject past check-in date");
    }

    @Test
    @DisplayName("T15 - Throws exception when check-out is same as check-in")
    void checkOutSameAsCheckIn() {
        Map<String, String> params = buildValidParams();
        String today = LocalDate.now().toString();
        params.put("checkInDate", today);
        params.put("checkOutDate", today);
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject same check-in and check-out date");
    }

    @Test
    @DisplayName("T16 - Throws exception when check-out is before check-in")
    void checkOutBeforeCheckIn() {
        Map<String, String> params = buildValidParams();
        params.put("checkInDate",  LocalDate.now().plusDays(3).toString());
        params.put("checkOutDate", LocalDate.now().plusDays(1).toString());
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject check-out before check-in");
    }

    @Test
    @DisplayName("T17 - Accepts future check-in and check-out dates")
    void validFutureDates() throws Exception {
        Map<String, String> params = buildValidParams();
        Reservation r = service.createReservation(params, "admin");
        assertNotNull(r);
        assertEquals(3, r.getNumNights(), "Should calculate 3 nights correctly");
    }

    // ============================================================
    // 5. BILLING CALCULATION TESTS
    // ============================================================

    @Test
    @DisplayName("T18 - Correct bill total for 3 nights Standard room")
    void billCalculationStandard3Nights() throws Exception {
        Map<String, String> params = buildValidParams(); // 3 nights, Standard = 5500/night
        Reservation r = service.createReservation(params, "admin");

        BigDecimal expected = new BigDecimal("16500.00"); // 3 * 5500
        assertEquals(0, expected.compareTo(r.getTotalAmount()),
            "Bill should be 3 × LKR 5,500 = LKR 16,500");
    }

    @Test
    @DisplayName("T19 - Correct bill total for 5 nights Deluxe room")
    void billCalculationDeluxe5Nights() throws Exception {
        Map<String, String> params = buildValidParams();
        params.put("roomType", "Deluxe");
        params.put("checkInDate",  LocalDate.now().plusDays(1).toString());
        params.put("checkOutDate", LocalDate.now().plusDays(6).toString()); // 5 nights

        Reservation r = service.createReservation(params, "admin");
        BigDecimal expected = new BigDecimal("42500.00"); // 5 * 8500
        assertEquals(0, expected.compareTo(r.getTotalAmount()),
            "Bill should be 5 × LKR 8,500 = LKR 42,500");
    }

    @Test
    @DisplayName("T20 - calculateBill returns correct breakdown with 10% tax")
    void calculateBillBreakdown() throws Exception {
        Reservation r = new Reservation();
        r.setReservationNumber("RES-20250001");
        r.setRoomType("Standard");
        r.setCheckInDate(LocalDate.now().plusDays(1));
        r.setCheckOutDate(LocalDate.now().plusDays(4)); // 3 nights
        r.setGuestName("Test Guest");
        r.setAddress("Test Address");
        r.setContactNumber("+94711234567");

        when(mockDao.getByReservationNumber("RES-20250001")).thenReturn(r);
        when(mockDao.getRoomTypeByName("Standard")).thenReturn(
            new RoomType(1, "Standard", new BigDecimal("5500.00"), "Garden view"));

        Map<String, Object> bill = service.calculateBill(r);

        assertEquals(3L, bill.get("numNights"));
        assertEquals(0, new BigDecimal("16500.00").compareTo((BigDecimal) bill.get("subtotal")));
        assertEquals(0, new BigDecimal("1650.00").compareTo((BigDecimal) bill.get("tax")));
        assertEquals(0, new BigDecimal("18150.00").compareTo((BigDecimal) bill.get("total")));
    }

    // ============================================================
    // 6. EDGE CASE TESTS
    // ============================================================

    @Test
    @DisplayName("T21 - Guest name with single character throws exception")
    void guestNameTooShort() {
        Map<String, String> params = buildValidParams();
        params.put("guestName", "A");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Guest name must be at least 2 characters");
    }

    @Test
    @DisplayName("T22 - Invalid room type throws exception")
    void invalidRoomType() throws Exception {
        when(mockDao.getRoomTypeByName("Unknown")).thenReturn(null);
        Map<String, String> params = buildValidParams();
        params.put("roomType", "Unknown");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject unknown room type");
    }

    @Test
    @DisplayName("T23 - Number of guests must be between 1 and 10")
    void invalidNumGuests() {
        Map<String, String> params = buildValidParams();
        params.put("numGuests", "11");
        assertThrows(IllegalArgumentException.class,
            () -> service.createReservation(params, "admin"),
            "Should reject more than 10 guests");
    }

    @Test
    @DisplayName("T24 - Reservation number is auto-generated correctly")
    void reservationNumberGenerated() throws Exception {
        Map<String, String> params = buildValidParams();
        Reservation r = service.createReservation(params, "admin");
        assertNotNull(r.getReservationNumber());
        assertTrue(r.getReservationNumber().startsWith("RES-"),
            "Reservation number should start with RES-");
    }

    @Test
    @DisplayName("T25 - Created-by is correctly assigned")
    void createdByAssigned() throws Exception {
        Map<String, String> params = buildValidParams();
        Reservation r = service.createReservation(params, "staff");
        assertEquals("staff", r.getCreatedBy(), "CreatedBy should match session user");
    }

    // ============================================================
    // Helper Method
    // ============================================================

    private Map<String, String> buildValidParams() {
        Map<String, String> params = new HashMap<>();
        params.put("guestName",       "Kamal Perera");
        params.put("address",         "123 Beach Road, Galle");
        params.put("contactNumber",   "+94771234567");
        params.put("email",           "kamal@email.com");
        params.put("roomType",        "Standard");
        params.put("checkInDate",     LocalDate.now().plusDays(1).toString());
        params.put("checkOutDate",    LocalDate.now().plusDays(4).toString()); // 3 nights
        params.put("numGuests",       "2");
        params.put("specialRequests", "Sea facing room preferred");
        return params;
    }
}
