package com.example.idp.service;

import com.example.common.coreapi.UserWithStatus;
import com.example.common.role.Roles;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Override
    public void createUser(UserWithStatus u) {
        jdbcUserDetailsManager.createUser(
                User.withUsername(u.getUsername()).password("{bcrypt}" + u.getPassword()).roles(Roles.USER_ROLE).build());
    }


    @Override
    public void deleteUser(String username) {
        jdbcUserDetailsManager.deleteUser(username);
    }


}
