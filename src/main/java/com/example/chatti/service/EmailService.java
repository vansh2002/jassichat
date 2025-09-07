package com.example.chatti.service;

import com.example.chatti.model.EmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(EmailResponse emailResponse) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailResponse.getFrom());
            message.setTo(emailResponse.getTo());
            message.setSubject(emailResponse.getSubject());
            message.setText(emailResponse.getTxt());
            emailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error Sending email : " + e.getMessage());
        }
    }
}
