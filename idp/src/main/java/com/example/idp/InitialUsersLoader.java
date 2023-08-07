package com.example.idp;

import com.example.common.role.Roles;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Profile("dev")
@AllArgsConstructor
public class InitialUsersLoader {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    private Environment env;

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("Development environment preparing. Loading sample data..");
        try {
            jdbcUserDetailsManager.createUser(
                    User.withUsername(env.getProperty("IDP_ADMIN")).password(
                            "{bcrypt}" + BCrypt.hashpw(env.getProperty("IDP_ADMIN_PASSWORD"), BCrypt.gensalt())).roles(
                            Roles.ADMIN_ROLE).build());
        } catch (DuplicateKeyException e) {
            log.info("users already exist");
        }

    }
}
