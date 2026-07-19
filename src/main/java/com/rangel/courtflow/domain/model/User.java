package com.rangel.courtflow.domain.model;

import lombok.Getter;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User {

    private final UUID id;
    private final String email;
    private final String password;
    private final UserRole role;

    public User(UUID id, String email, String password, UserRole role) {
        this.id = Objects.requireNonNull(id, "The user ID cannot be null");
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("The email cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("The password cannot be null or empty");
        }
        this.email = email;
        this.password = password;
        this.role = Objects.requireNonNull(role, "The user role cannot be null");
    }
}