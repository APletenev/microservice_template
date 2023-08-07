package com.example.idp.service;

import com.example.common.coreapi.UserWithStatus;
import com.example.common.role.Roles;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void createUser(UserWithStatus u) {
        jdbcUserDetailsManager.createUser(
                User.withUsername(u.getUsername()).password("{bcrypt}" + u.getPassword()).roles(
                        Roles.USER_ROLE).build());
        updateUserWithId(u.getUsername(), u.getId());
    }

    @Override
    @Transactional
    public void deleteUserById(UUID id) {
            String username = getUsernameById(id);
            if (username != null) {
                jdbcUserDetailsManager.deleteUser(username);
            }
    }


    @Override
    public void updateUserWithId(String username, UUID id) {
        if (id==null) id=UUID.randomUUID();
        String sql = "UPDATE users SET id = ? WHERE username = ?";
        jdbcTemplate.update(sql, id, username);
    }

    private String getUsernameById(UUID id) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (DataAccessException e) {
            //Record with this id has been not found
            return null;
        }
    }

}
