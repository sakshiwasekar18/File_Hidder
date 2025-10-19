package org.example.service;

import java.util.Random;

public class GenerateOTP {
    // Generate a 4-digit OTP
    public static String getOTP() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}
