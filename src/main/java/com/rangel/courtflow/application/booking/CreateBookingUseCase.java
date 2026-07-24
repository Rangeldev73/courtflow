package com.rangel.courtflow.application.booking;

import com.rangel.courtflow.domain.exception.BookingConflictException;
import com.rangel.courtflow.domain.exception.CourtNotFoundException;
import com.rangel.courtflow.domain.model.Booking;
import com.rangel.courtflow.domain.model.BookingStatus;
import com.rangel.courtflow.domain.model.Court;
import com.rangel.courtflow.domain.model.CourtStatus;
import com.rangel.courtflow.domain.model.TimeSlot;
import com.rangel.courtflow.infrastructure.config.RabbitMQConfig;
import com.rangel.courtflow.infrastructure.persistence.BookingJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.BookingJpaRepository;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.CourtJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.BookingRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.BookingMapper;
import com.rangel.courtflow.infrastructure.web.mapper.CourtMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CreateBookingUseCase {

    private final CourtJpaRepository courtJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private final RabbitTemplate rabbitTemplate;

    public CreateBookingUseCase(CourtJpaRepository courtJpaRepository,
                                BookingJpaRepository bookingJpaRepository,
                                RabbitTemplate rabbitTemplate) {
        this.courtJpaRepository = courtJpaRepository;
        this.bookingJpaRepository = bookingJpaRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public BookingResponseDTO execute(BookingRequestDTO requestDTO) {
        CourtJpaEntity courtEntity = courtJpaRepository.findById(requestDTO.courtId())
                .orElseThrow(() -> new CourtNotFoundException("Court not found with id: " + requestDTO.courtId()));

        Court court = CourtMapper.toDomain(courtEntity);

        if (court.getStatus() != CourtStatus.ACTIVE) {
            throw new IllegalStateException("Court is not active, cannot create booking");
        }

        TimeSlot newTimeSlot = new TimeSlot(requestDTO.start(), requestDTO.end());

        List<BookingJpaEntity> existingEntities = bookingJpaRepository.findByCourtIdAndStatusNotIn(
                requestDTO.courtId(),
                List.of(BookingStatus.CANCELLED, BookingStatus.EXPIRED)
        );

        boolean hasConflict = existingEntities.stream()
                .map(BookingMapper::toDomain)
                .anyMatch(existingBooking -> existingBooking.getTimeSlot().conflictsWith(newTimeSlot));

        if (hasConflict) {
            throw new BookingConflictException("The requested time slot conflicts with an existing booking for this court");
        }

        Booking booking = Booking.create(requestDTO.courtId(), newTimeSlot, requestDTO.customerId());

        BookingJpaEntity entityToSave = BookingMapper.toJpaEntity(booking);
        bookingJpaRepository.save(entityToSave);

        rabbitTemplate.convertAndSend(RabbitMQConfig.PENDING_QUEUE, booking.getId());

        return BookingMapper.toResponseDTO(booking);
    }
}