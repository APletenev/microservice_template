package com.example.account;

import com.example.account.entity.Account;
import com.example.account.service.AccountServiceImpl;
import com.example.common.coreapi.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class AccountApplication {

	@Autowired
	AccountServiceImpl accountService;
	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@Bean
	public Consumer<Credentials> signupEventSupplier() {
		return credentials -> accountService.save(new Account(credentials.getUsername(), credentials.getEmail(), 0));
	}

}
