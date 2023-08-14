package com.example.idp.service;

import com.example.common.coreapi.Credentials;
import com.example.common.coreapi.UserWithStatus;
import lombok.extern.apachecommons.CommonsLog;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static com.example.common.coreapi.UserStatus.CANCELLED;
import static com.example.common.coreapi.UserStatus.NEW;
import static java.lang.Thread.currentThread;

@Service
@CommonsLog
public class SignupSagaOrchestrator {

    private final UserService userService;

    private final StreamBridge streamBridge;

    private final Map<UUID, SignupResponseDTO> transactionMap = new ConcurrentHashMap<>();

    private final int timeout;

    public SignupSagaOrchestrator(UserService userService, StreamBridge streamBridge, Environment env) {
        this.userService = userService;
        this.streamBridge = streamBridge;
        this.timeout = env.getProperty("RESPONSE_TIMEOUT", Integer.class, 5);
    }

    public ResponseEntity<String> createSignupSaga(Credentials credentials) {

        UserWithStatus userWithStatus = new UserWithStatus(credentials, UUID.randomUUID(), NEW);
        // Shedule ransaction cancelling by timeout
        JobId jobId = BackgroundJob.schedule(Instant.now().plusSeconds(timeout),
                () -> cancelTransactionByTimeout(userWithStatus));

        CompletableFuture<ResponseEntity<String>> signupFuture = new CompletableFuture<>();
        transactionMap.put(userWithStatus.getId(), new SignupResponseDTO(signupFuture, jobId));

        streamBridge.send("signup-out-0", userWithStatus);

        try { // Wait for the signupFuture to complete
            return signupFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            if (e instanceof InterruptedException) currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup error");
        } finally {
            transactionMap.remove(userWithStatus.getId());
        }
    }

    public void cancelTransactionByTimeout(UserWithStatus userWithStatus) {
        UUID id = userWithStatus.getId();
        if (id != null) {
            SignupResponseDTO dto = transactionMap.get(id);
            if (dto != null) {
                CompletableFuture<ResponseEntity<String>> future = dto.getResponse();
                if (future != null) {
                    future.complete(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Signup error: Timeout"));
                }
                userWithStatus.setStatus(CANCELLED);
                streamBridge.send("signup-out-0", userWithStatus);
            }
        }
    }

    @Bean
    public Consumer<UserWithStatus> signup() {
        return u -> {
            switch (u.getStatus()) {
                case NEW -> {
                    try {
                        userService.createUser(u);
                    } catch (DuplicateKeyException e) {
                        CompletableFuture<ResponseEntity<String>> future = transactionMap.get(u.getId()).getResponse();
                        if (future != null)
                            future.complete(ResponseEntity.status(HttpStatus.CONFLICT).body(
                                    "Signup error: " + "User with name " + u.getUsername() + " already exists"));
                        log.info("Attempt to signup with existing username");
                    }
                }
                case CONFIRMED -> {
                    SignupResponseDTO dto = transactionMap.get(u.getId());
                    if (dto != null) {
                        dto.getResponse().complete(
                                ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully."));
                        BackgroundJob.delete(dto.getJobId());
                    }
                }
                case CANCELLED -> userService.deleteUserById(u.getId());

            }
        };
    }
}
