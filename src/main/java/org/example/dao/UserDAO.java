package org.example.dao;

import org.example.db.MyConnection;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public static boolean isExists(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT email FROM users");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String e = rs.getString(1);
            if (e.equals(email))
                return true;
        }
        return false;
    }

    public static int saveUser(User user) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users VALUES (default, ?, ?)"
        );
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        int result = ps.executeUpdate();
        LOGGER.info("User saved: " + user.getEmail());
        return result;
    }
}
