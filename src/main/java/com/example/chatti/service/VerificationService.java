package com.example.chatti.service;

import com.example.chatti.controller.WebSocketController;
import com.example.chatti.entity.ChattyEntity;
import com.example.chatti.model.EmailResponse;
import com.example.chatti.repository.ChattyRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @Autowired
    private ChattyRepository chattyRepository;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(VerificationService.class);

//    @Autowired
//    private TwilioSmsSender twilioSmsSender;

    public ResponseEntity<String> checkVerificationCode(Map<String, String> payload, HttpSession session) {
        String username = payload.get("username");
        String code = payload.get("code");

        String storedCode = verificationCodes.get(username);
        if (storedCode == null || !storedCode.equals(code)) {
            return new ResponseEntity<>("Invalid Code", HttpStatus.BAD_REQUEST);
        }

        WebSocketController.addVerifiedUser(username);

        Optional<ChattyEntity> chattyEntityOptional = chattyRepository.findByUsername(username);
        if(chattyEntityOptional.isPresent()) {
            ChattyEntity chatty = chattyEntityOptional.get();
            chatty.setVerified(true);
            chattyRepository.save(chatty);
        }

        verificationCodes.remove(username);
        session.setAttribute("verifiedUser", username);

        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    public ResponseEntity<String> sendVerificationCode(String username) {
        Optional<ChattyEntity> chattyEntityOptional = chattyRepository.findByUsername(username);

        if (chattyEntityOptional.isEmpty()) { return new ResponseEntity<>("No records found with this username", HttpStatus.BAD_REQUEST); }

        ChattyEntity chatty = chattyEntityOptional.get();
        if (chatty.isVerified()) {
            return new ResponseEntity<>("Password required", HttpStatus.OK);
        }

        String email = chatty.getEmail();
        System.out.println(email + ' ' + username);

        String code = String.valueOf((int)(Math.random() * 9000) + 1000);
        String messageBody = "Your ChatApp verification code is: " + code;

        try {
            EmailResponse emailResponse = EmailResponse.builder()
                    .from("chotabheemdholakpurwala@gmail.com")
                    .to(email)
                    .subject("OTP for JassiChill")
                    .txt(messageBody)
                    .build();
            emailService.sendEmail(emailResponse);

            verificationCodes.put(username, code);
            return new ResponseEntity<>("Verification Code Sent Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Failed to send verification email. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void removeVerificationCode(String username) {
        verificationCodes.remove(username);
    }

    private String getUsernameFromIdentity(String identity) {
        Optional<ChattyEntity> chattyByUsername = chattyRepository.findByUsername(identity);
        Optional<ChattyEntity> chattyByEmail = chattyRepository.findByEmail(identity);

        return chattyByUsername.isPresent() ? chattyByUsername.get().getUsername() : chattyByEmail.isPresent() ? chattyByEmail.get().getUsername() : "";
    }

    public ResponseEntity<String> verifyPassword(Map<String, String> payload, HttpSession session) {
        Optional<ChattyEntity> chattyEntityOptional = chattyRepository.findByUsername(payload.get("username"));

        if(chattyEntityOptional.isEmpty()) {return new ResponseEntity<>("No records found with this username", HttpStatus.BAD_REQUEST); }

        ChattyEntity chatty = chattyEntityOptional.get();

        if(!payload.get("password").equals(chatty.getPassword())) {
            System.out.println("Password does not match");
            return ResponseEntity.badRequest().body("Password does not match");
        }

        System.out.println("logged in successfully");
        session.setAttribute("verifiedUser", payload.get("username"));
        WebSocketController.addVerifiedUser(payload.get("username"));

        return ResponseEntity.ok().body(payload.get("username"));
    }
}