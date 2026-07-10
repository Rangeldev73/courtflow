package com.rangel.courtflow.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeSlotTest {
    @Test
    void shouldDetectConflictWhenTimeSlotsOverlap() {
        LocalDateTime start1 = LocalDateTime.of(2026, 7, 10, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 7, 10, 11, 0);
        TimeSlot slot1 = new TimeSlot(start1, end1);

        LocalDateTime start2 = LocalDateTime.of(2026, 7, 10, 10, 30);
        LocalDateTime end2 = LocalDateTime.of(2026, 7, 10, 11, 30);
        TimeSlot slot2 = new TimeSlot(start2, end2);

        boolean isConflicting = slot1.conflictsWith(slot2);

        assertTrue(isConflicting, "Timeslops should conflict and return true");
    }

    @Test
    void shouldNotDetectConflictWhenTimeSlotsAreCompletelySeparate(){
        LocalDateTime start1 = LocalDateTime.of(2026, 7, 10, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 7, 10, 11, 0);
        TimeSlot slot1 = new TimeSlot(start1, end1);

        LocalDateTime start2 = LocalDateTime.of(2026, 7, 10, 14, 30);
        LocalDateTime end2 = LocalDateTime.of(2026, 7, 10, 16, 30);
        TimeSlot slot2 = new TimeSlot(start2, end2);

        boolean isConflicting = slot1.conflictsWith(slot2);

        assertFalse(isConflicting, "Timeslops should not conflict and return false");
    }

    @Test
    void shouldNotDetectConflictWhenTimeSlotsAreAdjacent(){
        LocalDateTime start1 = LocalDateTime.of(2026, 7, 10, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 7, 10, 11, 0);
        TimeSlot slot1 = new TimeSlot(start1, end1);

        LocalDateTime start2 = LocalDateTime.of(2026, 7, 10, 11, 0);
        LocalDateTime end2 = LocalDateTime.of(2026, 7, 10, 12, 30);
        TimeSlot slot2 = new TimeSlot(start2, end2);

        boolean isConflicting = slot1.conflictsWith(slot2);

        assertFalse(isConflicting, "Timeslops should not conflict and return false");
    }

    @Test
    void shouldDetectConflictWhenOneTimeSlotIsFullyContainedInAnother(){
        LocalDateTime start1 = LocalDateTime.of(2026, 7, 10, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 7, 10, 16, 0);
        TimeSlot slot1 = new TimeSlot(start1, end1);

        LocalDateTime start2 = LocalDateTime.of(2026, 7, 10, 11, 30);
        LocalDateTime end2 = LocalDateTime.of(2026, 7, 10, 12, 30);
        TimeSlot slot2 = new TimeSlot(start2, end2);

        boolean isConflicting = slot1.conflictsWith(slot2);

        assertTrue(isConflicting, "Timeslops should conflict and return true");
    }
}