package com.example.idp.service;

import com.example.common.coreapi.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class SignupSagaOrchestrator {
    private UserService userService;
    private final StreamBridge streamBridge;
    private final Map<String, CompletableFuture<Boolean>> transactionMap = new ConcurrentHashMap<>();
    public void createSignupSaga(Credentials credentials) throws Exception {

        userService.createUser(credentials); // In case of user exists the saga will stop by exception and according error will be returned to controller

        CompletableFuture<Boolean> signupFuture = new CompletableFuture<>();
        transactionMap.put(credentials.getUsername(), signupFuture);
        streamBridge.send("createAccount-out-0", credentials);

        // Wait for 'accountCreated' message or timeout
        try {
            signupFuture.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // Timeout occurred, cancel the transaction
            cancelTransaction(credentials.getUsername());
            throw new Exception("Registering error: " + e.getMessage());
        }
    }

    @Bean
    public Consumer<String> accountCreated() {
        return username -> {
            CompletableFuture<Boolean> future = transactionMap.get(username);
            if (future != null) {
                future.complete(true);
                transactionMap.remove(username);
            }
        };
    }

    private void cancelTransaction(String username) {
        userService.deleteUser(username);
        transactionMap.remove(username);
    }
}
