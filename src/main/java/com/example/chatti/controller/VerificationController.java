package com.example.chatti.controller;

import com.example.chatti.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VerificationController {

    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/api/verify/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> payload) {
        System.out.println("In Verification Controller" + payload);
        return verificationService.sendVerificationCode(payload.get("username"));
    }

    @PostMapping("/api/verify/check-code")
    public ResponseEntity<String> checkVerificationCode(@RequestBody Map<String, String> payload, HttpSession session) {
        return verificationService.checkVerificationCode(payload, session);
    }

    // request from frontend will come here if email is verified
    @PostMapping("/api/verify/password")
    public ResponseEntity<String> verifyPassword(@RequestBody Map<String, String> payload, HttpSession session) {
        return verificationService.verifyPassword(payload, session);
    }
}
