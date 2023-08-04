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
        UserWithStatus result = new UserWithStatus(s1.getUsername(), s1.getPassword(), s1.getEmail(), CONFIRMED);
        if (s2 == null || s2.getStatus() == CANCELLED) result.setStatus(CANCELLED);
        return result;
    }
}