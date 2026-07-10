package com.rangel.courtflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourtJpaRepository extends JpaRepository<CourtJpaEntity, UUID> {
}
