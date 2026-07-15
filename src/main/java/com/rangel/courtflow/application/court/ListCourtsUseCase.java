package com.rangel.courtflow.application.court;

import com.rangel.courtflow.infrastructure.persistence.CourtJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.CourtMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListCourtsUseCase {

    private final CourtJpaRepository courtJpaRepository;

    public ListCourtsUseCase(CourtJpaRepository courtJpaRepository) {
        this.courtJpaRepository = courtJpaRepository;
    }

    public List<CourtResponseDTO> execute() {
        return courtJpaRepository.findAll().stream()
                .map(CourtMapper::toDomain)
                .map(CourtMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}