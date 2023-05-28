package com.example.common.coreapi.events;

import lombok.Data;

@Data
public class UserDeletedEvent {

    private final String userId;
    private final String username;
}
