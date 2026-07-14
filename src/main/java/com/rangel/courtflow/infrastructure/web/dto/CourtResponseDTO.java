package com.rangel.courtflow.infrastructure.web.dto;

import com.rangel.courtflow.domain.model.CourtStatus;
import com.rangel.courtflow.domain.model.Sport;
import java.util.Set;
import java.util.UUID;

public record CourtResponseDTO(
        UUID id,
        String name,
        CourtStatus status,
        Set<Sport> sports
) {}