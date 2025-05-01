package com.tourstravels.controller;

import com.tourstravels.service.PasswordResetService;
import com.tourstravels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tours_travel/forgot-password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String response = passwordResetService.sendOtp(email);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        String response = passwordResetService.verifyOtp(email, otp);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        String response = passwordResetService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok(Map.of("message", response));
    }

}
