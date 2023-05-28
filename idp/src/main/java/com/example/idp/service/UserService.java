package com.example.idp.service;

import com.example.common.coreapi.events.UserCreatedEvent;
import com.example.common.coreapi.events.UserDeletedEvent;

public interface UserService {
    void createUser(UserCreatedEvent event);

    void deleteUser(UserDeletedEvent event);

}
