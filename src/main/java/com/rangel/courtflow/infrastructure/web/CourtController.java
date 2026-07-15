package com.rangel.courtflow.infrastructure.web;

import com.rangel.courtflow.application.court.ActivateCourtUseCase;
import com.rangel.courtflow.application.court.CreateCourtUseCase;
import com.rangel.courtflow.application.court.DeactivateCourtUseCase;
import com.rangel.courtflow.application.court.FindCourtByIdUseCase;
import com.rangel.courtflow.application.court.ListCourtsUseCase;
import com.rangel.courtflow.infrastructure.web.dto.CourtRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CreateCourtUseCase createCourtUseCase;
    private final ListCourtsUseCase listCourtsUseCase;
    private final FindCourtByIdUseCase findCourtByIdUseCase;
    private final DeactivateCourtUseCase deactivateCourtUseCase;
    private final ActivateCourtUseCase activateCourtUseCase;

    public CourtController(CreateCourtUseCase createCourtUseCase,
                           ListCourtsUseCase listCourtsUseCase,
                           FindCourtByIdUseCase findCourtByIdUseCase,
                           DeactivateCourtUseCase deactivateCourtUseCase,
                           ActivateCourtUseCase activateCourtUseCase) {
        this.createCourtUseCase = createCourtUseCase;
        this.listCourtsUseCase = listCourtsUseCase;
        this.findCourtByIdUseCase = findCourtByIdUseCase;
        this.deactivateCourtUseCase = deactivateCourtUseCase;
        this.activateCourtUseCase = activateCourtUseCase;
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

    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> findById(@PathVariable UUID id) {
        CourtResponseDTO courtResponseDTO = findCourtByIdUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(courtResponseDTO);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CourtResponseDTO> deactivate(@PathVariable UUID id) {
        CourtResponseDTO courtResponseDTO = deactivateCourtUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(courtResponseDTO);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CourtResponseDTO> activate(@PathVariable UUID id) {
        CourtResponseDTO courtResponseDTO = activateCourtUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(courtResponseDTO);
    }
}