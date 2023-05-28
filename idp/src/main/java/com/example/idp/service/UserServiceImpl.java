package com.example.idp.service;

import com.example.common.Roles;
import com.example.common.coreapi.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Override
    public void createUser(Credentials credentials) {
        jdbcUserDetailsManager.createUser(
                User.withUsername(credentials.getUsername()).password("{bcrypt}" + credentials.getPassword()).roles(Roles.USER_ROLE).build());
    }


    @Override
    public void deleteUser(String username) {
        jdbcUserDetailsManager.deleteUser(username);
    }


}
