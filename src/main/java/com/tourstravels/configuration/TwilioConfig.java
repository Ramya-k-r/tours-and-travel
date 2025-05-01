package com.tourstravels.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;  // Use Jakarta's version of PostConstruct

@Configuration
public class TwilioConfig {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void twilioInit() {
        Twilio.init(accountSid, authToken);  // Initialize Twilio with SID and Auth Token
    }

    public String getTwilioPhoneNumber() {
        return twilioPhoneNumber;
    }
}
