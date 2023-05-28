package com.example.idp.controller;

import com.example.common.coreapi.UserDTO;
import com.example.common.coreapi.commands.SignupUserCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestMapping("#{environment.IDP_API}")
@RestController
@AllArgsConstructor
public class UserController {

//    private UserService userService;

    private CommandGateway commandGateway;

    private QueryGateway queryGateway;

    @PostMapping("/signup")
    public CompletableFuture<Void> signUpUser(@RequestBody UserDTO credentials) {
        return commandGateway.send(new SignupUserCommand(UUID.randomUUID().toString(),credentials.getUsername(), credentials.getPassword(), credentials.getEmail()));
    }

}
