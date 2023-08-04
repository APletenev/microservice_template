package com.example.idp.service;

import com.example.common.coreapi.UserWithStatus;

public interface UserService {

    void createUser(UserWithStatus u);

    void deleteUser(String username);
}
