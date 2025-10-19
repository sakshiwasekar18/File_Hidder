package org.example.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendOTPService {
    private static final Logger LOGGER = Logger.getLogger(SendOTPService.class.getName());

    public static void sendOTP(String email, String genOTP) {
        String from = "wasekarsakshi@gmail.com"; // Your sender email
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "enter your password"); // your app password
            }
        });

        session.setDebug(false); // set true if you want debug logs

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("File Enc App OTP");
            message.setText("Your One Time Password (OTP) is: " + genOTP);

            Transport.send(message);
            LOGGER.info("OTP sent successfully to " + email);
        } catch (MessagingException mex) {
            LOGGER.log(Level.SEVERE, "Failed to send OTP to " + email, mex);
        }
    }
}
