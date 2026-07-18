package com.rangel.courtflow.infrastructure.web.mapper;

import com.rangel.courtflow.domain.model.Booking;
import com.rangel.courtflow.domain.model.TimeSlot;
import com.rangel.courtflow.infrastructure.persistence.BookingJpaEntity;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;

public class BookingMapper {

    public static Booking toDomain(BookingJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        TimeSlot timeSlot = new TimeSlot(entity.getStartTime(), entity.getEndTime());

        return Booking.reconstruct(
                entity.getId(),
                entity.getCourtId(),
                timeSlot,
                entity.getCustomerId(),
                entity.getStatus()
        );
    }

    public static BookingJpaEntity toJpaEntity(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingJpaEntity entity = new BookingJpaEntity();
        entity.setId(booking.getId());
        entity.setCourtId(booking.getCourtId());
        entity.setCustomerId(booking.getCustomerId());
        entity.setStatus(booking.getStatus());
        entity.setStartTime(booking.getTimeSlot().getStart());
        entity.setEndTime(booking.getTimeSlot().getEnd());

        return entity;
    }

    public static BookingResponseDTO toResponseDTO(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingResponseDTO(
                booking.getId(),
                booking.getCourtId(),
                booking.getCustomerId(),
                booking.getTimeSlot().getStart(),
                booking.getTimeSlot().getEnd(),
                booking.getStatus()
        );
    }
}
