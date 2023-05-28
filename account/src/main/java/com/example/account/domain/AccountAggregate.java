package com.example.account.domain;

import com.example.common.coreapi.commands.CreateAccountCommand;
import com.example.common.coreapi.events.AccountCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@SuppressWarnings("FieldCanBeLocal")
@NoArgsConstructor
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private String ownerName;
    private String email;
    private long balance;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOwnerName(), command.getEmail()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        accountId = event.getAccountId();
        ownerName = event.getOwnerName();
        email = event.getEmail();
        balance = 0;
    }

}
