package com.rangel.courtflow.application.booking;


import com.rangel.courtflow.infrastructure.persistence.BookingJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.BookingMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListBookingsByCourtUseCase {

    private final BookingJpaRepository bookingJpaRepository;

    public ListBookingsByCourtUseCase(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    public List<BookingResponseDTO> execute(UUID courtId) {
        return bookingJpaRepository.findByCourtId(courtId).stream()
                .map(BookingMapper::toDomain)
                .map(BookingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
