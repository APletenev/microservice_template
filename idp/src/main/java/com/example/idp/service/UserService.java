package com.example.idp.service;

import com.example.common.coreapi.UserWithStatus;

import java.util.UUID;

public interface UserService {

    void createUser(UserWithStatus u);

    void deleteUserById(UUID id);

    void updateUserWithId(String username, UUID id);
}
