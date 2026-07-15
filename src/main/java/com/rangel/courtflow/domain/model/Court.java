package com.rangel.courtflow.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class Court {

    private final UUID id;
    private final String name;
    private final Set<Sport> sports;
    private CourtStatus status;

    public Court(UUID id, String name, Set<Sport> sports, CourtStatus status) {
        if(id == null) {
            throw new IllegalArgumentException("The court ID cannot be null");
        }
        if(name == null || name.strip().isEmpty()) {
            throw new IllegalArgumentException("The court name cannot be null or empty");
        }
        if(sports == null || sports.isEmpty()) {
            throw new IllegalArgumentException("The court must accommodate at least one sport");
        }
        if(status == null) {
            throw new IllegalArgumentException("The initial status of the court cannot be null");
        }

        this.id = id;
        this.name = name.strip();
        this.sports = Set.copyOf(sports);
        this.status = status;
    }

    public void deactivate() {
        if(this.status == CourtStatus.INACTIVE) {
            throw new IllegalStateException("The court is already inactive");
        }
        this.status = CourtStatus.INACTIVE;
    }

    public void activate() {
        if (this.status == CourtStatus.ACTIVE) {
            throw new IllegalStateException("The court is already active");
        }
        this.status = CourtStatus.ACTIVE;
    }
}
