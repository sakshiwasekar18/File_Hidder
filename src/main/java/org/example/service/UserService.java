package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public static int saveUser(User user) {
        try {
            if (UserDAO.isExists(user.getEmail())) {
                LOGGER.info("User already exists: " + user.getEmail());
                return 0; // User already exists
            } else {
                return UserDAO.saveUser(user); // Insert user into DB
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getEmail(), ex);
            return -1; // Error occurred
        }
    }
}
