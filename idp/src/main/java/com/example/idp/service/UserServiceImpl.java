package com.example.idp.service;

import com.example.common.Roles;
import com.example.common.coreapi.events.UserCreatedEvent;
import com.example.common.coreapi.events.UserDeletedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @EventHandler
    @Override
    public void createUser(UserCreatedEvent event) {
        jdbcUserDetailsManager.createUser(
                User.withUsername(event.getUsername()).password("{bcrypt}" + event.getPassword()).roles(Roles.USER_ROLE).build());
    }

    @EventHandler
    @Override
    public void deleteUser(UserDeletedEvent event) {
        jdbcUserDetailsManager.deleteUser(event.getUsername());
    }


}
