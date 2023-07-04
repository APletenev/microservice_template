package com.example.idp.domain.aggregate;

import com.example.common.role.Roles;
import com.example.common.coreapi.commands.CreateUserCommand;
import com.example.common.coreapi.commands.RollbackUserCreationCommand;
import com.example.common.coreapi.commands.SignupUserCommand;
import com.example.common.coreapi.events.UserCreatedEvent;
import com.example.common.coreapi.events.UserDeletedEvent;
import com.example.common.coreapi.events.UserSignedUpEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@SuppressWarnings("FieldCanBeLocal")
@NoArgsConstructor
@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private String userId;
    private String username;
    private String password;
    private Boolean enabled;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Set<String> roles;

    @CommandHandler
    public UserAggregate(SignupUserCommand command) {
        apply(new UserSignedUpEvent(command.getUserId(), command.getUsername(), command.getPassword(), command.getEmail()));
    }
    @CommandHandler
    public void handle(CreateUserCommand command) {
        apply(new UserCreatedEvent(command.getUserId(), command.getUsername(), command.getPassword()));
    }

    @CommandHandler
    public void handle(RollbackUserCreationCommand command) {
        apply(new UserDeletedEvent(command.getUserId(), command.getUsername()));
    }

    @EventSourcingHandler
    public void on(UserSignedUpEvent event) {

        userId = event.getUserId();
        username = event.getUsername();
        password = event.getPassword();
        enabled = true;
        roles = new HashSet<>();
        roles.add(Roles.USER_ROLE);
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event){
        markDeleted();
    }
}

