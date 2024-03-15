package org.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String input = "Hello, world!";
        String hash = computeSHA256(input);

        System.out.println("Original text: " + input);
        System.out.println("SHA-256 hash: " + hash);
    }

    public static String computeSHA256(String text) {
        try {
            // Step 1: Get a SHA-256 hashing tool ready
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Step 2: Feed the text into the hashing tool as bytes using UTF-8 encoding
            byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            // Step 3: Convert the hashed bytes into a readable hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Convert byte to hex (two characters)
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where the algorithm is not available (rare for SHA-256)
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}