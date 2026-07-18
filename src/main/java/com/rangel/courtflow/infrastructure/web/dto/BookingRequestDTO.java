package com.rangel.courtflow.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRequestDTO(
        @NotNull UUID courtId,
        @NotNull UUID customerId,
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end
) {}