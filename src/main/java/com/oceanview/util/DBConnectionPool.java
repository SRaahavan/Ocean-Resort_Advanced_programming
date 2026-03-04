package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionPool {

    private static DBConnectionPool instance;

    private String url;
    private String username;
    private String password;

    private DBConnectionPool(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    public static synchronized DBConnectionPool getInstance(String url, String username, String password) {
        if (instance == null) {
            instance = new DBConnectionPool(url, username, password);
        }
        return instance;
    }

    public static DBConnectionPool getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DBConnectionPool not initialized. Call getInstance(url, user, pass) first.");
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
