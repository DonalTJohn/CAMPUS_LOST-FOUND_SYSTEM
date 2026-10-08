package com.lostfound.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 * Implements the Singleton/Factory pattern for central connection management.
 */
public class DBConnection {

    // Configurable Database Constants
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lost_found_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Life@#21"; // Update this with your local MySQL password if different

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure mysql-connector-j is included in dependencies.");
            e.printStackTrace();
        }
    }

    // Private constructor to prevent direct instantiation
    private DBConnection() {}

    /**
     * Obtains a fresh database Connection instance.
     * 
     * @return Connection object to MySQL database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Safely closes an AutoCloseable resource (Connection, Statement, ResultSet).
     * 
     * @param resource AutoCloseable resource to close
     */
    public static void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
