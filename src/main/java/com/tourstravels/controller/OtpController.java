package com.tourstravels.controller;

import com.tourstravels.entity.User;
import com.tourstravels.service.OtpService;
import com.tourstravels.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tours_travel")
public class OtpController {

    private final UserService userService;
    private final OtpService otpService;

    public OtpController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String contact = request.get("contact"); 

        if (contact == null || contact.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mobile number or Email is required!"));
        }

        // Check if the email or mobile is registered
        Optional<User> user = userService.findByEmailOrMobile(contact);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not registered!"));
        }

        boolean sent = otpService.sendOtp(contact);
        if (sent) {
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully!"));
        } else {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to send OTP!"));
        }
    }

 

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        String contact = request.get("contact");
        String otp = request.get("otp");

        if (contact == null || contact.isEmpty() || otp == null || otp.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Contact and OTP are required!"));
        }

        try {
            boolean verified = otpService.verifyOtp(contact, otp);
            if (verified) {
                return ResponseEntity.ok(Map.of("message", "OTP verified successfully!"));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid OTP!"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
    
}
