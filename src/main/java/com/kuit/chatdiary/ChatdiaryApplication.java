package com.kuit.chatdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatdiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatdiaryApplication.class, args);
	}

}
