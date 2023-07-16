package com.example.idp.controller;

import com.example.common.coreapi.Credentials;
import com.example.idp.service.SignupSagaOrchestrator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static com.example.common.ApiEndpoints.SIGNUP_ENDPOINT;

@RequestMapping("#{environment.IDP_API}")
@RestController
@AllArgsConstructor
public class UserController {
    private SignupSagaOrchestrator signupSagaOrchestrator;
    @PostMapping(SIGNUP_ENDPOINT)
    public CompletableFuture<ResponseEntity<String>> signUpUser(@RequestBody Credentials credentials) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                signupSagaOrchestrator.createSignupSaga(credentials);
                return ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully.");
            } catch (DuplicateKeyException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Signup error: " + "User with name " + credentials.getUsername() + " already exists");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup error: " + e.getMessage());
            }
        });
    }

}
