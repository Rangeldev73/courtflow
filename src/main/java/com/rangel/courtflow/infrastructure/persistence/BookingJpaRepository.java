package com.rangel.courtflow.infrastructure.persistence;

import com.rangel.courtflow.domain.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, UUID> {
    List<BookingJpaEntity> findByCourtIdAndStatusNotIn(UUID courtId, List<BookingStatus> excludedStatuses);
}