package com.example.chatti.controller;

import com.example.chatti.model.ChattyRequest;
import com.example.chatti.service.RegisterChattyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterChattyController {

    @Autowired
    private RegisterChattyService registerChattyService; // Inject the service

    @PostMapping("/api/register")
    public ResponseEntity<String> registerChatty(@RequestBody ChattyRequest chattyRequest) {
        // Call the service method to handle the logic
        System.out.println(chattyRequest);
        return registerChattyService.registerChatty(chattyRequest);
    }
}