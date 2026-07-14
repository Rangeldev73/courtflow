package com.rangel.courtflow.infrastructure.web.dto;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String message,
        Instant timestamp,
        List<String> errors
) {}