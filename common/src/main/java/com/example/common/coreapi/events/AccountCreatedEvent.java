package com.example.common.coreapi.events;

import lombok.Data;

@Data
public class AccountCreatedEvent {
    private final String accountId;
    private final String ownerName;
    private final String email;
}
