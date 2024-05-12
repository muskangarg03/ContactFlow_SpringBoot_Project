package com.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    // Twilio Account SID and Auth Token obtained from Twilio console
    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    // Twilio phone number obtained from Twilio console
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void makeVoiceCall(String toPhoneNumber, String message) {
        // Initialize Twilio client with account SID and auth token
        Twilio.init(accountSid, authToken);

        // Make a voice call using Twilio API
        Call call = Call.creator(
                new PhoneNumber(toPhoneNumber), // Recipient phone number
                new PhoneNumber(twilioPhoneNumber), // Twilio phone number
                new com.twilio.type.Twiml("<Response><Say>" + message + "</Say></Response>")) // TwiML for voice call
                .create();

        // Print call SID
        System.out.println("Call SID: " + call.getSid());
    }
}
