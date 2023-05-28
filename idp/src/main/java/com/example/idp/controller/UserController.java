package com.example.idp.controller;

import com.example.common.coreapi.Credentials;
import com.example.idp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("#{environment.IDP_API}")
@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final StreamBridge streamBridge;

    @PostMapping("/signup")
    public void signUpUser(@RequestBody Credentials credentials) {

        userService.createUser(credentials);
        streamBridge.send("signupEventSupplier-out-0", credentials);

    }

}
