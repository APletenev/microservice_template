package com.example.common.coreapi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Credentials {
    private final String username;
    private final String password;
    private final String email;
}
