package org.example.views;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.service.GenerateOTP;
import org.example.service.SendOTPService;
import org.example.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Welcome {
    private static final Logger LOGGER = Logger.getLogger(Welcome.class.getName());

    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the FileHidder app");

        System.out.println("Press 1 to signup");   // Swapped
        System.out.println("Press 2 to login");    // Swapped
        System.out.println("Press 0 to exit");

        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Invalid input", ex);
        }

        switch (choice) {
            case 1 -> signUp();   // Signup is now 1
            case 2 -> login();    // Login is now 2
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid choice");
        }
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email:");
        String email = sc.nextLine();

        try {
            if (UserDAO.isExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);

                System.out.println("Enter the OTP sent to your email:");
                String otp = sc.nextLine();

                if (otp.equals(genOTP)) {
                    System.out.println("Welcome!");
                    new UserView(email).home();
                } else {
                    System.out.println("Wrong OTP");
                }
            } else {
                System.out.println("User not found");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Login failed for email: " + email, ex);
        }
    }

    private void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();

        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);

        System.out.println("Enter the OTP sent to your email:");
        String otp = sc.nextLine();

        if (otp.equals(genOTP)) {
            User user = new User(name, email);
            int response = UserService.saveUser(user);

            switch (response) {
                case 0 -> System.out.println("User already exists");
                case 1 -> System.out.println("User registered successfully");
                case -1 -> System.out.println("Error occurred while registering user");
            }
        } else {
            System.out.println("Wrong OTP");
        }
    }
}
