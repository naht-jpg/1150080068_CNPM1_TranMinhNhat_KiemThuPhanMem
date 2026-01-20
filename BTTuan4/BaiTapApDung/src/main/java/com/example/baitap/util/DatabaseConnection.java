package com.example.baitap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối PostgreSQL
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/organization_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection = null;

    /**
     * Lấy kết nối đến database
     * @return Connection object
     * @throws SQLException nếu không thể kết nối
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL Driver not found", e);
            }
        }
        return connection;
    }

    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiểm tra kết nối có hoạt động không
     * @return true nếu kết nối OK
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
