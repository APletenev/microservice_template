package com.example.idp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IdpApplication {


	public static void main(String[] args) {
		SpringApplication.run(IdpApplication.class, args);
	}

}
