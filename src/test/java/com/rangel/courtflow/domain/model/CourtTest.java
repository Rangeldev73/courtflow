package com.rangel.courtflow.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourtTest {
    void shouldCreateValidCourtSuccessfully() {
        UUID id = UUID.randomUUID();
        String name = "Quadra Central Coberta";
        Set<Sport> sports = Set.of(Sport.FUTSAL, Sport.TENIS);
        CourtStatus status = CourtStatus.ACTIVE;

        Court court = new Court(id, name, sports, status);

        assertEquals(id, court.getId());
        assertEquals(name, court.getName());
        assertEquals(sports, court.getSports());
        assertEquals(CourtStatus.ACTIVE, court.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        UUID id = UUID.randomUUID();
        Set<Sport> sports = Set.of(Sport.FUTSAL);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Court(id, null, sports, CourtStatus.ACTIVE);
        });

        assertEquals("The court name cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSportsListIsEmpty() {
        UUID id = UUID.randomUUID();
        String name = "Quadra de Tênis 1";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Court(id, name, Collections.emptySet(), CourtStatus.ACTIVE);
        });

        assertEquals("The court must accommodate at least one sport", exception.getMessage());
    }

    @Test
    void shouldDeactivateActiveCourt() {
        Court court = new Court(UUID.randomUUID(), "Quadra 3", Set.of(Sport.VOLEI), CourtStatus.ACTIVE);

        court.deactivate();

        assertEquals(CourtStatus.INACTIVE, court.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingAlreadyInactiveCourt() {
        Court court = new Court(UUID.randomUUID(), "Quadra 4", Set.of(Sport.VOLEI), CourtStatus.INACTIVE);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            court.deactivate();
        });

        assertEquals("The court is already inactive", exception.getMessage());
    }
}
