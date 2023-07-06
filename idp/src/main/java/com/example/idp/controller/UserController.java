package com.example.idp.controller;

import com.example.common.coreapi.UserDTO;
import com.example.common.coreapi.commands.SignupUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.example.common.ApiEndpoints.SIGNUP_ENDPOINT;

@RequestMapping("#{environment.IDP_API}")
@RestController
@AllArgsConstructor
public class UserController {

    private CommandGateway commandGateway;

    @PostMapping(SIGNUP_ENDPOINT)
    public CompletableFuture<ResponseEntity<String>> signUpUser(@RequestBody UserDTO credentials) {
        return commandGateway.send(new SignupUserCommand(UUID.randomUUID().toString(), credentials.getUsername(), credentials.getPassword(), credentials.getEmail()))
                .thenApply(result -> new ResponseEntity<>("User created successfully", HttpStatus.CREATED))
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause();
                    if (cause instanceof CommandExecutionException) {
                        String errorMessage = cause.getMessage();
                        HttpStatus statusCode = (HttpStatus) ((CommandExecutionException) cause).getDetails().orElse(HttpStatus.INTERNAL_SERVER_ERROR);
                        return ResponseEntity.status(statusCode).body(errorMessage);
                    } else {
                        String errorMessage = "An error occurred";
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                    }
                });
    }

}
