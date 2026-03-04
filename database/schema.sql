CREATE DATABASE IF NOT EXISTS ocean_view_resort
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ocean_view_resort;


-- Admin / Staff Table

CREATE TABLE IF NOT EXISTS admin (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    role        ENUM('ADMIN','STAFF') DEFAULT 'STAFF',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Default admin credentials: admin / admin123
INSERT INTO admin (username, password, full_name, role)
VALUES ('admin', 'admin123', 'System Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO admin (username, password, full_name, role)
VALUES ('staff', 'staff123', 'Front Desk Staff', 'STAFF')
ON DUPLICATE KEY UPDATE username = username;


-- ============================================================
-- Room Types Table
-- ============================================================
CREATE TABLE IF NOT EXISTS room_types (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    type_name   VARCHAR(50)  NOT NULL UNIQUE,
    rate_per_night DECIMAL(10,2) NOT NULL,
    description VARCHAR(255)
);

INSERT INTO room_types (type_name, rate_per_night, description) VALUES
('Standard',  5500.00,  'Comfortable standard room with garden view'),
('Deluxe',    8500.00,  'Spacious deluxe room with partial sea view'),
('Sea View',  12000.00, 'Premium room with full ocean view and balcony'),
('Suite',     20000.00, 'Luxury suite with living area and private pool access')
ON DUPLICATE KEY UPDATE type_name = type_name;


-- ============================================================
-- Reservations Table
-- ============================================================
CREATE TABLE IF NOT EXISTS reservations (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    reservation_number  VARCHAR(20) NOT NULL UNIQUE,
    guest_name          VARCHAR(100) NOT NULL,
    address             TEXT NOT NULL,
    contact_number      VARCHAR(20)  NOT NULL,
    email               VARCHAR(100),
    room_type           VARCHAR(50)  NOT NULL,
    check_in_date       DATE NOT NULL,
    check_out_date      DATE NOT NULL,
    num_guests          INT  DEFAULT 1,
    special_requests    TEXT,
    status              ENUM('CONFIRMED','CHECKED_IN','CHECKED_OUT','CANCELLED') DEFAULT 'CONFIRMED',
    total_amount        DECIMAL(10,2),
    created_by          VARCHAR(50),
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (room_type) REFERENCES room_types(type_name)
);

-- ============================================================
-- Stored Procedure: Get Reservation with Bill
-- ============================================================
DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS GetReservationWithBill(IN res_num VARCHAR(20))
BEGIN
    SELECT r.*,
           rt.rate_per_night,
           DATEDIFF(r.check_out_date, r.check_in_date) AS num_nights,
           (DATEDIFF(r.check_out_date, r.check_in_date) * rt.rate_per_night) AS calculated_total
    FROM reservations r
    JOIN room_types rt ON r.room_type = rt.type_name
    WHERE r.reservation_number = res_num;
END $$

DELIMITER ;
