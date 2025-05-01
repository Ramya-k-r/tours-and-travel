package com.tourstravels.service;

import com.tourstravels.entity.PasswordReset;
import com.tourstravels.repository.PasswordResetRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Generate and send OTP
    public String sendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(900000) + 100000); // Ensures 6-digit OTP
        
        Optional<PasswordReset> existingRecord = passwordResetRepository.findByEmail(email);
        PasswordReset passwordReset = existingRecord.orElse(new PasswordReset());

        passwordReset.setEmail(email);
        passwordReset.setOtp(otp);
        passwordResetRepository.save(passwordReset);

        sendOtpEmail(email, otp);
        return "OTP sent successfully!";
    }

    // Send OTP via email
    private void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset OTP");
            helper.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for a limited time.");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    // Verify OTP
    public String verifyOtp(String email, String otp) {
        Optional<PasswordReset> record = passwordResetRepository.findByEmail(email);
        if (record.isEmpty() || !record.get().getOtp().equals(otp)) {
            return "Invalid OTP!";
        }
        return "OTP verified successfully!";
    }

    // Reset Password
    public String resetPassword(String email, String otp, String newPassword) {
        Optional<PasswordReset> record = passwordResetRepository.findByEmail(email);
        if (record.isEmpty() || !record.get().getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        // Encode and update the password
        userService.updateUserPassword(email, passwordEncoder.encode(newPassword));

        // Delete OTP record after successful reset
        passwordResetRepository.delete(record.get());
        return "Password reset successful!";
    }
}
