package com.example.idp.service;

import com.example.common.coreapi.Credentials;

public interface UserService {

    void createUser(Credentials credentials);

    void deleteUser(String username);
}
