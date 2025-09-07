package com.example.chatti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChattiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattiApplication.class, args);
	}

}
