package com.example.chatti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChattyVerificationRequest {
    private String email;
    private String password;
}
