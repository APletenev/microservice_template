package com.example.common.coreapi;

import lombok.Data;

@Data
public class Credentials {
    private final String username;
    private final String password;
    private final String email;
}
