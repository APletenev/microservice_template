package com.example.idp.service;

import com.example.common.coreapi.Credentials;
import com.example.common.coreapi.UserWithStatus;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static com.example.common.coreapi.UserStatus.CANCELLED;
import static com.example.common.coreapi.UserStatus.NEW;

@Service
@CommonsLog
public class SignupSagaOrchestrator {

    private final UserService userService;

    private final StreamBridge streamBridge;

    private final Map<String, CompletableFuture<ResponseEntity<String>>> transactionMap = new ConcurrentHashMap<>();

    private final int timeout;

    public SignupSagaOrchestrator(UserService userService, StreamBridge streamBridge, Environment env) {
        this.userService = userService;
        this.streamBridge = streamBridge;
        this.timeout = env.getProperty("RESPONSE_TIMEOUT", Integer.class, 5);
    }

    public ResponseEntity<String> createSignupSaga(Credentials credentials) {

        CompletableFuture<ResponseEntity<String>> signupFuture = new CompletableFuture<>();
        transactionMap.put(credentials.getUsername(), signupFuture);
        UserWithStatus userWithStatus = new UserWithStatus(credentials, NEW);
        streamBridge.send("signup-out-0", userWithStatus);
        boolean exceptionRaised = false;

        try { // Wait for the signupFuture to complete
            return signupFuture.get(timeout, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            exceptionRaised = true;
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Signup error: Timeout.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exceptionRaised = true;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup error.");
        } finally {
            if (exceptionRaised) {
                userWithStatus.setStatus(CANCELLED);
                streamBridge.send("signup-out-0", userWithStatus);
            }
            transactionMap.remove(credentials.getUsername());
        }
    }

    @Bean // Provides reliable Kafka-based "scheduler" for cancelling transaction by timeout
    public KStream<String, UserWithStatus> accountResponse(StreamsBuilder builder, UserStatusJoiner userStatusJoiner) {
        JsonSerde<UserWithStatus> userSerde = new JsonSerde<>(UserWithStatus.class);
        KStream<String, UserWithStatus> stream = builder.stream("signup", Consumed.with(Serdes.String(), userSerde))
                .selectKey((key, value) -> value.getUsername())
                .filter((k, v) -> (v.getStatus() == NEW));
        stream.leftJoin(
                        builder.stream("accountCreation", Consumed.with(Serdes.String(), userSerde))
                                .selectKey((key, value) -> value.getUsername()),
                        userStatusJoiner,
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(timeout)),
                        StreamJoined.with(Serdes.String(), userSerde, userSerde))
                .to("signup", Produced.with(Serdes.String(), userSerde));
        return stream;
    }

    @Bean
    public Consumer<UserWithStatus> signup() {
        return u -> {
            switch (u.getStatus()) {
                case NEW -> {
                    try {
                        userService.createUser(u);
                    } catch (DuplicateKeyException e) {
                        CompletableFuture<ResponseEntity<String>> future = transactionMap.get(u.getUsername());
                        if (future != null)
                            future.complete(ResponseEntity.status(HttpStatus.CONFLICT).body(
                                    "Signup error: " + "User with name " + u.getUsername() + " already exists"));
                        log.info("Attempt to signup with existing username");
                    }
                }
                case CONFIRMED -> {
                    CompletableFuture<ResponseEntity<String>> future = transactionMap.get(u.getUsername());
                    if (future != null)
                        future.complete(ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully."));
                }
                case CANCELLED -> userService.deleteUser(u.getUsername());

            }
        };
    }
}
