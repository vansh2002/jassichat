package com.example.chatti.service;//package com.example.chatapp.service;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TwilioSmsSender {
//
//    // Your Twilio Account SID and Auth Token from twilio.com/console
//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.phone.number}")
//    private String twilioPhoneNumber;
//
//    @PostConstruct
//    public void init() {
//        Twilio.init(accountSid, authToken);
//    }
//
//    public void sendSms(String toPhoneNumber, String messageBody) {
//        try {
//            Message message = Message.creator(
//                    new PhoneNumber(toPhoneNumber),
//                    new PhoneNumber(twilioPhoneNumber),
//                    messageBody
//            ).create();
//
//            System.out.println("SMS sent successfully with SID: " + message.getSid());
//        } catch (Exception e) {
//            System.err.println("Failed to send SMS: " + e.getMessage());
//        }
//    }
//}
