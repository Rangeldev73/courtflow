package com.rangel.courtflow.infrastructure.web.dto;

import com.rangel.courtflow.domain.model.Sport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CourtRequestDTO(
        @NotBlank @Size(max = 255) String name,
        @NotEmpty Set<Sport> sports
) {}