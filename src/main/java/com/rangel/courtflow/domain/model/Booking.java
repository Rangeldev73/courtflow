package com.rangel.courtflow.domain.model;

import lombok.Getter;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Booking {

    private final UUID id;
    private final UUID courtId;
    private final TimeSlot timeSlot;
    private final UUID customerId;
    private BookingStatus status;

    private Booking(UUID id, UUID courtId, TimeSlot timeSlot, UUID customerId, BookingStatus status) {
        this.id = Objects.requireNonNull(id, "The booking ID cannot be null");
        this.courtId = Objects.requireNonNull(courtId, "The court ID cannot be null");
        this.timeSlot = Objects.requireNonNull(timeSlot, "The time slot cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "The customer ID cannot be null");
        this.status = Objects.requireNonNull(status, "The booking status cannot be null");
    }

    public static Booking create(UUID courtId, TimeSlot timeSlot, UUID customerId) {
        return new Booking(
                UUID.randomUUID(),
                courtId,
                timeSlot,
                customerId,
                BookingStatus.PENDING
        );
    }

    public static Booking reconstruct(UUID id, UUID courtId, TimeSlot timeSlot, UUID customerId, BookingStatus status) {
        return new Booking(id, courtId, timeSlot, customerId, status);
    }

    public void confirm() {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be confirmed. Current status: " + this.status);
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("The booking is already cancelled");
        }
        if (this.status == BookingStatus.EXPIRED) {
            throw new IllegalStateException("Cannot cancel an expired booking");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void expire() {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can expire. Current status: " + this.status);
        }
        this.status = BookingStatus.EXPIRED;
    }
}