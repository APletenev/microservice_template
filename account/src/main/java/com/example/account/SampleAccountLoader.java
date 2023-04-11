package com.example.account;

import com.example.account.entity.Account;
import com.example.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Profile("dev")
@AllArgsConstructor
public class SampleAccountLoader {
    private AccountService accountService;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("Development environment. Loading sample data..");
        accountService.save(new Account("user", "user@example.com",0));

    }
}
