package com.rangel.courtflow.application.court;

import com.rangel.courtflow.domain.exception.CourtNotFoundException;
import com.rangel.courtflow.domain.model.Court;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.CourtMapper;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeactivateCourtUseCase {

    private final CourtJpaRepository courtJpaRepository;

    public DeactivateCourtUseCase(CourtJpaRepository courtJpaRepository) {
        this.courtJpaRepository = courtJpaRepository;
    }

    public CourtResponseDTO execute(UUID id) {
        CourtJpaEntity entity = courtJpaRepository.findById(id)
                .orElseThrow(() -> new CourtNotFoundException("Court not found with id: " + id));
        Court court = CourtMapper.toDomain(entity);
        court.deactivate();
        CourtJpaEntity updatedEntity = CourtMapper.toJpaEntity(court);
        CourtJpaEntity savedEntity = courtJpaRepository.save(updatedEntity);
        Court savedCourt = CourtMapper.toDomain(savedEntity);
        return CourtMapper.toResponseDTO(savedCourt);
    }
}