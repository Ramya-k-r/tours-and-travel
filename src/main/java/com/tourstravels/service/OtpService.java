package com.tourstravels.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.tourstravels.configuration.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final TwilioConfig twilioConfig;
    private final JavaMailSender mailSender;

    private static final long OTP_EXPIRY_TIME_MS = TimeUnit.MINUTES.toMillis(5); // 5 minutes expiry
    private static final ConcurrentHashMap<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    @Autowired
    public OtpService(TwilioConfig twilioConfig, JavaMailSender mailSender) {
        this.twilioConfig = twilioConfig;
        this.mailSender = mailSender;
    }

    private static class OtpData {
        String otp;
        long timestamp;

        OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }

    private String normalizeContact(String contact) {
        if (contact.contains("@")) {
            return contact.toLowerCase(); // Normalize emails
        } else if (!contact.startsWith("+")) {
            return "+91" + contact; // Normalize mobile numbers
        }
        return contact;
    }

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public boolean sendOtp(String contact) {
        String normalizedContact = normalizeContact(contact);
        String otp = generateOtp();

        if (contact.contains("@")) {
            return sendOtpByEmail(normalizedContact, otp);
        } else {
            return sendOtpByMobile(normalizedContact, otp);
        }
    }

    private boolean sendOtpByMobile(String mobile, String otp) {
        try {
            Message.creator(
                new PhoneNumber(mobile),
                new PhoneNumber(twilioConfig.getTwilioPhoneNumber()),
                "Your OTP is: " + otp + ". It expires in 5 minutes."
            ).create();

            otpStorage.put(mobile, new OtpData(otp, System.currentTimeMillis()));
            System.out.println("OTP Sent to: " + maskContact(mobile));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendOtpByEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp + ". It expires in 5 minutes.");
            mailSender.send(message);

            otpStorage.put(email, new OtpData(otp, System.currentTimeMillis()));
            System.out.println("OTP Sent to: " + maskContact(email));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOtp(String contact, String otp) {
        String normalizedContact = normalizeContact(contact);
        OtpData otpData = otpStorage.get(normalizedContact);

        if (otpData != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - otpData.timestamp > OTP_EXPIRY_TIME_MS) {
                otpStorage.remove(normalizedContact);
                return false; // OTP expired
            }
            if (otpData.otp.equals(otp)) {
                otpStorage.remove(normalizedContact);
                return true;
            }
        }
        return false;
    }

    private String maskContact(String contact) {
        if (contact.contains("@")) {
            return contact.replaceAll("(?<=.{2}).(?=.*@)", "*");
        } else {
            return contact.replaceAll("(?<=\\+91\\d{2})\\d(?=\\d{2})", "*");
        }
    }
}
