package com.example.common.coreapi.commands;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private final String accountId;
    private final String ownerName;
    private final String email;
}
