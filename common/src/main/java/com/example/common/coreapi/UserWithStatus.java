package com.example.common.coreapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
public class UserWithStatus extends Credentials{
    private UUID id;
    private UserStatus status;
    public UserWithStatus(Credentials credentials, UUID id, UserStatus aNew) {
        super (credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
        this.id =id;
        this.status=aNew;
    }

    @JsonCreator
    public UserWithStatus(@JsonProperty("id") UUID id, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("email") String email, @JsonProperty("status") UserStatus status) {
        super(username, password, email);
        this.id =id;
        this.status = status;
    }
}
