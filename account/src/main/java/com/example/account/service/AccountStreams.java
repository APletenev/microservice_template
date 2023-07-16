package com.example.account.service;

import com.example.account.entity.Account;
import com.example.common.coreapi.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class AccountStreams {

    AccountServiceImpl accountService;
    private StreamBridge streamBridge;

    @Bean
    public Consumer<Credentials> createAccount() {
        return credentials -> {
            accountService.save(new Account(credentials.getUsername(), credentials.getEmail(), 0));
            streamBridge.send("accountCreated-out-0",credentials.getUsername());
        };
    }
}
