package com.rangel.courtflow.infrastructure.web.dto;

import com.rangel.courtflow.domain.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponseDTO(
        UUID id,
        UUID courtId,
        UUID customerId,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status
) {}
