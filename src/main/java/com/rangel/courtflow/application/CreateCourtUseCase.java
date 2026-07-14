package com.rangel.courtflow.application;

import com.rangel.courtflow.domain.model.Court;
import com.rangel.courtflow.domain.model.CourtStatus;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.CourtRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.CourtMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateCourtUseCase {

    private final CourtJpaRepository courtJpaRepository;

    public CreateCourtUseCase(CourtJpaRepository courtJpaRepository) {
        this.courtJpaRepository = courtJpaRepository;
    }

    public CourtResponseDTO execute(CourtRequestDTO requestDTO) {
        UUID id = UUID.randomUUID();
        Court court = new Court(id, requestDTO.name(), requestDTO.sports(), CourtStatus.ACTIVE);
        CourtJpaEntity courtJpaEntity = CourtMapper.toJpaEntity(court);
        CourtJpaEntity savedEntity = courtJpaRepository.save(courtJpaEntity);
        Court savedCourt = CourtMapper.toDomain(savedEntity);
        return CourtMapper.toResponseDTO(savedCourt);
    }
}
