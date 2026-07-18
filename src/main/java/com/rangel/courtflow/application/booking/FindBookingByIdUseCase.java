package com.rangel.courtflow.application.booking;


import com.rangel.courtflow.domain.exception.BookingNotFoundException;
import com.rangel.courtflow.domain.model.Booking;
import com.rangel.courtflow.infrastructure.persistence.BookingJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.BookingJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.BookingMapper;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class FindBookingByIdUseCase {

    private final BookingJpaRepository bookingJpaRepository;

    public FindBookingByIdUseCase(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    public BookingResponseDTO execute(UUID id) {
        BookingJpaEntity entity = bookingJpaRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));

        Booking booking = BookingMapper.toDomain(entity);
        return BookingMapper.toResponseDTO(booking);
    }
}
