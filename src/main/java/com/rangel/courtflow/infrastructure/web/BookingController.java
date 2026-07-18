package com.rangel.courtflow.infrastructure.web;


import com.rangel.courtflow.application.booking.CancelBookingUseCase;
import com.rangel.courtflow.application.booking.ConfirmBookingUseCase;
import com.rangel.courtflow.application.booking.CreateBookingUseCase;
import com.rangel.courtflow.application.booking.FindBookingByIdUseCase;
import com.rangel.courtflow.application.booking.ListBookingsByCourtUseCase;
import com.rangel.courtflow.infrastructure.web.dto.BookingRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.BookingResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final FindBookingByIdUseCase findBookingByIdUseCase;
    private final ListBookingsByCourtUseCase listBookingsByCourtUseCase;

    public BookingController(CreateBookingUseCase createBookingUseCase,
                             ConfirmBookingUseCase confirmBookingUseCase,
                             CancelBookingUseCase cancelBookingUseCase,
                             FindBookingByIdUseCase findBookingByIdUseCase,
                             ListBookingsByCourtUseCase listBookingsByCourtUseCase) {
        this.createBookingUseCase = createBookingUseCase;
        this.confirmBookingUseCase = confirmBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.findBookingByIdUseCase = findBookingByIdUseCase;
        this.listBookingsByCourtUseCase = listBookingsByCourtUseCase;
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
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> findById(@PathVariable UUID id) {
        BookingResponseDTO bookingResponseDTO = findBookingByIdUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> listByCourt(@RequestParam UUID courtId) {
        List<BookingResponseDTO> bookings = listBookingsByCourtUseCase.execute(courtId);
        return ResponseEntity.status(HttpStatus.OK).body(bookings);
    }
}