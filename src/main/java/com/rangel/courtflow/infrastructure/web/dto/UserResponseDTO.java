package com.rangel.courtflow.infrastructure.web.dto;

import com.rangel.courtflow.domain.model.UserRole;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        UserRole role
) {}