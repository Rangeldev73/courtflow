package com.rangel.courtflow.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TimeSlot {

    LocalDateTime start;
    LocalDateTime end;

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public boolean conflictsWith(TimeSlot other) {
        if (other == null) {
            return false;
        }
        return this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }
}
