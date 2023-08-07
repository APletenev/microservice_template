package com.example.account.service;

import com.example.account.entity.Account;
import com.example.common.coreapi.UserWithStatus;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

import static com.example.common.coreapi.UserStatus.CONFIRMED;

@Service
@AllArgsConstructor
@CommonsLog
public class AccountStreams {

    AccountServiceImpl accountService;
    private StreamBridge streamBridge;

    @Bean
    public Consumer<UserWithStatus> signup() {
        return u -> {
            switch (u.getStatus()) {
                case NEW -> {
                    try {
                        accountService.save(new Account(u.getId(), u.getUsername(), u.getEmail(), 0));
                        u.setStatus(CONFIRMED);
                        streamBridge.send("accountCreation-out-0", u);
                    } catch (Exception e) {
                        log.info("Attempt to signup with existing username");
                    }
                }
                case CANCELLED ->  accountService.deleteAccountById(u.getId());

                case CONFIRMED -> {/*No actions required*/}
            }
        };
    }
}