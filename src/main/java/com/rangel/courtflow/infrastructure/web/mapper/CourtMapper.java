package com.rangel.courtflow.infrastructure.web.mapper;

import com.rangel.courtflow.domain.model.Court;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaEntity;
import com.rangel.courtflow.infrastructure.web.dto.CourtResponseDTO;

public class CourtMapper {

    public static CourtResponseDTO toResponseDTO(Court court) {
        if(court == null) {
            return null;
        }

        return new CourtResponseDTO(
                court.getId(),
                court.getName(),
                court.getStatus(),
                court.getSports()
        );
    }

    public static CourtJpaEntity toJpaEntity(Court court) {
        if(court == null) {
            return null;
        }

        CourtJpaEntity entity = new CourtJpaEntity();
        entity.setId(court.getId());
        entity.setName(court.getName());
        entity.setStatus(court.getStatus());
        entity.setSports(court.getSports());

        return entity;
    }

    public static Court toDomain(CourtJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Court(
                entity.getId(),
                entity.getName(),
                entity.getSports(),
                entity.getStatus()
        );
    }
}
