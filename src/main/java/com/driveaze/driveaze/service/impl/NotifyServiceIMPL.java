package com.driveaze.driveaze.service.impl;

import com.driveaze.driveaze.service.interfac.NotifyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class NotifyServiceIMPL implements NotifyService {

    private static final String API_URL = "https://app.notify.lk/api/v1/send";
    private static final String API_KEY = "72T2naFFM33jTX9Nf7ho";  // Replace with your actual API key
    private static final String USER_ID = "28576";  // Replace with your actual user ID
    private static final String SENDER_ID = "NotifyDEMO";  // Replace with your actual sender ID
    private static final Map<String, String> otpCache = new HashMap<>();  // Temporary cache to store OTPs for verification
    private static final long OTP_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5);  // OTP expiration time (5 minutes)

    private final RestTemplate restTemplate;

    public NotifyServiceIMPL(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        otpCache.put(phoneNumber, otp);

        // Format phone number if needed (remove + or add country code if not present)
        String formattedPhone = phoneNumber.startsWith("+94") ?
                phoneNumber.substring(1) : phoneNumber.startsWith("0") ?
                "94" + phoneNumber.substring(1) : phoneNumber;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", USER_ID);
        requestBody.put("api_key", API_KEY);
        requestBody.put("sender_id", SENDER_ID);
        requestBody.put("to", formattedPhone);
        requestBody.put("message", "Your OTP For Samarasinghe Motors (pvt) LTD is: " + otp);

        // Create the request entity
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Make POST request instead of GET
            String response = restTemplate.postForObject(API_URL, request, String.class);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP", e);
        }
    }

    @Override
    public boolean verifyOtp(String phoneNumber, String otp) {
        // Check if the OTP is valid and not expired
        String storedOtp = otpCache.get(phoneNumber);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is valid, clear it from cache
            otpCache.remove(phoneNumber);
            return true;
        }

        // OTP is either invalid or expired
        return false;
    }

    // Sample OTP generation method (you can customize as needed)
    private String generateOtp() {
        // Generate a random 6-digit OTP
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
