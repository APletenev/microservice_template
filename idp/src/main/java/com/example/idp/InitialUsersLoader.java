package com.example.idp;

import com.example.common.role.Roles;
import com.example.idp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Profile("dev")
@AllArgsConstructor
@CommonsLog
public class InitialUsersLoader {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    private Environment env;

    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void runAfterStartup() {
        log.info("Development environment preparing. Loading sample data..");
        try {
            jdbcUserDetailsManager.createUser(
                    User.withUsername(env.getProperty("IDP_ADMIN")).password(
                            "{bcrypt}" + BCrypt.hashpw(env.getProperty("IDP_ADMIN_PASSWORD"), BCrypt.gensalt())).roles(
                            Roles.ADMIN_ROLE).build());
            userService.updateUserWithId(env.getProperty("IDP_ADMIN"), UUID.randomUUID());
        } catch (DuplicateKeyException e) {
            log.info("users already exist");
        }

    }
}
