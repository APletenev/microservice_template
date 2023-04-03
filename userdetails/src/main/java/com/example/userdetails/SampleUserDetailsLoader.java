package com.example.userdetails;

import com.example.userdetails.entity.UserDetails;
import com.example.userdetails.service.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Profile("dev")
@AllArgsConstructor
public class SampleUserDetailsLoader {
    private UserDetailsService userDetailsService;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("Development environment. Loading sample data..");
        userDetailsService.save(new UserDetails("user", "user@example.com",0));

    }
}
