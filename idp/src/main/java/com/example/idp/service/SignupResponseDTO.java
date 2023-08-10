package com.example.idp.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jobrunr.jobs.JobId;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Getter
public class SignupResponseDTO {

    private final CompletableFuture<ResponseEntity<String>> response;

    private final JobId jobId;
}
