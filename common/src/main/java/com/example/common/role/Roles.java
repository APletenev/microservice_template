package com.example.common.role;

public final class Roles {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private Roles() {
        throw new IllegalStateException("Utility class should not be instantiated");
    }
}
