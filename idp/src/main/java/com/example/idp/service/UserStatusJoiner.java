package com.example.idp.service;

import com.example.common.coreapi.UserWithStatus;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Service;

import static com.example.common.coreapi.UserStatus.CONFIRMED;
import static com.example.common.coreapi.UserStatus.CANCELLED;

@Service
public class UserStatusJoiner implements ValueJoiner<UserWithStatus, UserWithStatus, UserWithStatus> {

    @Override
    public UserWithStatus apply(UserWithStatus s1, UserWithStatus s2) {
        if (s2 == null || s2.getStatus() == CANCELLED) s1.setStatus(CANCELLED);
        else s1.setStatus(CONFIRMED);
        return s1;
    }
}