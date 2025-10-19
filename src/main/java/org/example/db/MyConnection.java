package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConnection {

    private static Connection connection = null;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/file?useSSL=false&serverTimezone=UTC",
                        "root",
                        "enter your password"
                );
                LOGGER.info("Database connection established successfully!");
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "MySQL Driver not found. Add it to your dependencies.", e);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database connection failed. Check URL, username, and password.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing the database connection.", e);
            }
        }
    }
}
