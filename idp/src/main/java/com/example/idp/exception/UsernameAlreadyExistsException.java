package com.example.idp.exception;

import org.axonframework.commandhandling.CommandExecutionException;

public class UsernameAlreadyExistsException extends CommandExecutionException {
    public UsernameAlreadyExistsException(String message, Throwable cause, Object details) {

        super(message, cause, details);
    }
}