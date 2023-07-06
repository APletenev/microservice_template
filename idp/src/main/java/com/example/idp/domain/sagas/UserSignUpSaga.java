package com.example.idp.domain.sagas;

import com.example.common.coreapi.commands.CreateAccountCommand;
import com.example.common.coreapi.commands.RollbackUserCreationCommand;
import com.example.common.coreapi.events.AccountCreatedEvent;
import com.example.common.coreapi.events.UserCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Saga
public class UserSignUpSaga {

    private static final String USERID_ASSOCIATION = "userId";
    private static final String ACCOUNTID_ASSOCIATION = "accountId";
    private static final String USER_STORE_DEADLINE = "UserStoreDeadline";

    @Autowired
    private transient CommandGateway commandGateway;
    private String userId;
    private String username;
    private String userStoreDeadlineId;
    private boolean userCreated;


    @StartSaga
    @SagaEventHandler(associationProperty = USERID_ASSOCIATION)
    public void handle(UserCreatedEvent userCreatedEvent, DeadlineManager deadlineManager) {
        userCreated = true;
        this.userId = userCreatedEvent.getUserId();
        this.username = userCreatedEvent.getUsername();
        String accountId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith(ACCOUNTID_ASSOCIATION, accountId);
        this.userStoreDeadlineId = deadlineManager.schedule(Duration.of(30, ChronoUnit.SECONDS), USER_STORE_DEADLINE);
        commandGateway.send(new CreateAccountCommand(accountId, this.username, userCreatedEvent.getEmail()));
    }

    @SagaEventHandler(associationProperty = ACCOUNTID_ASSOCIATION)
    public void handle(AccountCreatedEvent accountCreatedEvent, DeadlineManager deadlineManager) {
        deadlineManager.cancelSchedule(USER_STORE_DEADLINE, this.userStoreDeadlineId);
        SagaLifecycle.end();
    }

    //Rollback user creation in case account has not been created (saga has not been ended)
    @DeadlineHandler(deadlineName = USER_STORE_DEADLINE)
    @EndSaga
    public void on() {
        if (userCreated) {
            commandGateway.send(new RollbackUserCreationCommand(this.userId, this.username));
        }
    }

}

