package com.rangel.courtflow.infrastructure.web;

import com.rangel.courtflow.application.court.CreateCourtUseCase;
import com.rangel.courtflow.application.court.ListCourtsUseCase;
import com.rangel.courtflow.infrastructure.web.dto.CourtRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CreateCourtUseCase createCourtUseCase;
    private final ListCourtsUseCase listCourtsUseCase;

    public CourtController(CreateCourtUseCase createCourtUseCase, ListCourtsUseCase listCourtsUseCase) {
        this.createCourtUseCase = createCourtUseCase;
        this.listCourtsUseCase = listCourtsUseCase;
    }

    @PostMapping
    public ResponseEntity<CourtResponseDTO> create(@Valid @RequestBody CourtRequestDTO requestDTO) {
        CourtResponseDTO courtResponseDTO = createCourtUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(courtResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CourtResponseDTO>> list() {
        List<CourtResponseDTO> courts = listCourtsUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(courts);
    }
}