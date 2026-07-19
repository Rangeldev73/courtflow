package com.rangel.courtflow.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void shouldCreateValidUserSuccessfully() {
        UUID id = UUID.randomUUID();
        String email = "rangel@example.com";
        String password = "hashed-password-value";
        UserRole role = UserRole.CUSTOMER;

        User user = new User(id, email, password, role);

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User(null, "rangel@example.com", "hashed-password", UserRole.CUSTOMER);
        });
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(UUID.randomUUID(), null, "hashed-password", UserRole.CUSTOMER);
        });
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(UUID.randomUUID(), "   ", "hashed-password", UserRole.CUSTOMER);
        });
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(UUID.randomUUID(), "rangel@example.com", null, UserRole.CUSTOMER);
        });
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(UUID.randomUUID(), "rangel@example.com", "   ", UserRole.CUSTOMER);
        });
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new User(UUID.randomUUID(), "rangel@example.com", "hashed-password", null);
        });
    }
}
