package com.rangel.courtflow.infrastructure.web;


import com.rangel.courtflow.application.booking.CancelBookingUseCase;
import com.rangel.courtflow.application.booking.ConfirmBookingUseCase;
import com.rangel.courtflow.application.booking.CreateBookingUseCase;
import com.rangel.courtflow.infrastructure.web.dto.BookingRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;

    public BookingController(CreateBookingUseCase createBookingUseCase,
                             ConfirmBookingUseCase confirmBookingUseCase,
                             CancelBookingUseCase cancelBookingUseCase) {
        this.createBookingUseCase = createBookingUseCase;
        this.confirmBookingUseCase = confirmBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody BookingRequestDTO requestDTO) {
        BookingResponseDTO responseDTO = createBookingUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirm(@PathVariable UUID id) {
        BookingResponseDTO bookingResponseDTO = confirmBookingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponseDTO);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancel(@PathVariable UUID id) {
        BookingResponseDTO bookingResponseDTO = cancelBookingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponseDTO);
    }
}