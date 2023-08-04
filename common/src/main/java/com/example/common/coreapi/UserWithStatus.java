package com.example.common.coreapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
public class UserWithStatus extends Credentials{
    private UserStatus status;
    public UserWithStatus(Credentials credentials, UserStatus aNew) {
        super (credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
        this.status=aNew;
    }

    @JsonCreator
    public UserWithStatus(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("email") String email, @JsonProperty("status") UserStatus status) {
        super(username, password, email);
        this.status = status;
    }
}
