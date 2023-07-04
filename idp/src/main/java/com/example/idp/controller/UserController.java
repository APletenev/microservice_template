package com.example.idp.controller;

import com.example.common.coreapi.UserDTO;
import com.example.common.coreapi.commands.SignupUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
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
                .thenApply(result -> new ResponseEntity<>("User created successfully", HttpStatus.CREATED));
    }

}
