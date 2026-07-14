package com.rangel.courtflow.infrastructure.web;

import com.rangel.courtflow.application.CreateCourtUseCase;
import com.rangel.courtflow.infrastructure.web.dto.CourtRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CreateCourtUseCase createCourtUseCase;

    public CourtController(CreateCourtUseCase createCourtUseCase) {
        this.createCourtUseCase = createCourtUseCase;
    }

    @PostMapping
    public ResponseEntity<CourtResponseDTO> create(@Valid @RequestBody CourtRequestDTO requestDTO) {
        CourtResponseDTO courtResponseDTO = createCourtUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(courtResponseDTO);
    }
}
