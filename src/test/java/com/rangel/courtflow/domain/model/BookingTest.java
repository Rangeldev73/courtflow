package com.rangel.courtflow.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookingTest {
    private TimeSlot createDummyTimeSlot() {
        return new TimeSlot(
                LocalDateTime.of(2026, 7, 10, 14, 0),
                LocalDateTime.of(2026, 7, 10, 15, 0)
        );
    }

    @Test
    void shouldCreateNewBookingAsPendingWithGeneratedId() {
        UUID courtId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        TimeSlot timeSlot = createDummyTimeSlot();

        Booking booking = Booking.create(courtId, timeSlot, customerId);

        assertNotNull(booking.getId(), "The booking ID cannot be null");
        assertEquals(courtId, booking.getCourtId());
        assertEquals(customerId, booking.getCustomerId());
        assertEquals(timeSlot, booking.getTimeSlot());
        assertEquals(BookingStatus.PENDING, booking.getStatus(), "A new booking should start as PENDING");
    }

    @Test
    void shouldReconstructExistingBookingWithoutChangingStateOrId() {
        UUID originalId = UUID.randomUUID();
        UUID courtId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        TimeSlot timeSlot = createDummyTimeSlot();
        BookingStatus historyStatus = BookingStatus.CONFIRMED;

        Booking booking = Booking.reconstruct(originalId, courtId, timeSlot, customerId, historyStatus);

        assertEquals(originalId, booking.getId(), "The reconstructed id should be the exact same as original");
        assertEquals(historyStatus, booking.getStatus(), "The reconstructed status should be te same as original");
    }

    @Test
    void shouldConfirmPendingBookingSuccessfully() {
        Booking booking = Booking.create(UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID());

        booking.confirm();

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void shouldCancelPendingBookingSuccessfully() {
        Booking booking = Booking.create(UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID());

        booking.cancel();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void shouldExpirePendingBookingSuccessfully() {
        Booking booking = Booking.create(UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID());

        booking.expire();

        assertEquals(BookingStatus.EXPIRED, booking.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenConfirmingAlreadyConfirmedBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.CONFIRMED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::confirm);
        assertTrue(exception.getMessage().contains("Only PENDING bookings can be confirmed"));
    }

    @Test
    void shouldThrowExceptionWhenConfirmingCancelledBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::confirm);
        assertTrue(exception.getMessage().contains("Only PENDING bookings can be confirmed"));
    }

    @Test
    void shouldThrowExceptionWhenConfirmingExpiredBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.EXPIRED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::confirm);
        assertTrue(exception.getMessage().contains("Only PENDING bookings can be confirmed"));
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::cancel);
        assertEquals("The booking is already cancelled", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCancellingExpiredBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.EXPIRED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::cancel);
        assertEquals("Cannot cancel an expired booking", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenExpiringAlreadyConfirmedBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.CONFIRMED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::expire);
        assertTrue(exception.getMessage().contains("Only PENDING bookings can expire"));
    }

    @Test
    void shouldThrowExceptionWhenExpiringAlreadyCancelledBooking() {
        Booking booking = Booking.reconstruct(UUID.randomUUID(), UUID.randomUUID(), createDummyTimeSlot(), UUID.randomUUID(), BookingStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, booking::expire);
        assertTrue(exception.getMessage().contains("Only PENDING bookings can expire"));
    }
}
