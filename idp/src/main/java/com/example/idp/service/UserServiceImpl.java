package com.example.idp.service;

import com.example.common.coreapi.events.UserCreatedEvent;
import com.example.common.coreapi.events.UserDeletedEvent;
import com.example.common.coreapi.queries.UserExistsQuery;
import com.example.common.role.Roles;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @QueryHandler
    public Boolean handle(UserExistsQuery query) {
        return jdbcUserDetailsManager.userExists(query.getUsername());
    }

    @EventHandler
    @Override
    public void createUser(UserCreatedEvent event) {
        try {
            jdbcUserDetailsManager.loadUserByUsername(event.getUsername());
        } catch (UsernameNotFoundException e) {
            jdbcUserDetailsManager.createUser(
                    User.withUsername(event.getUsername()).password("{bcrypt}" + event.getPassword()).roles(Roles.USER_ROLE).build());
        }
    }

    @EventHandler
    @Override
    public void deleteUser(UserDeletedEvent event) {
        jdbcUserDetailsManager.deleteUser(event.getUsername());
    }


}
