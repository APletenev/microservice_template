package com.example.common.coreapi.commands;

import lombok.Data;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class SignupUserCommand {
    @TargetAggregateIdentifier
    private final String userId;
    private final String username;
    private final String password;
    private final String email;
}
