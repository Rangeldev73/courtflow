package com.rangel.courtflow.infrastructure.web;

import com.rangel.courtflow.domain.exception.BookingConflictException;
import com.rangel.courtflow.domain.exception.BookingNotFoundException;
import com.rangel.courtflow.domain.exception.CourtNotFoundException;
import com.rangel.courtflow.domain.exception.EmailAlreadyInUseException;
import com.rangel.courtflow.domain.exception.InvalidCredentialsException;
import com.rangel.courtflow.infrastructure.web.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        return buildBadRequestResponse("Validation failed", errors);
    }

    @ExceptionHandler(CourtNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCourtNotFound(CourtNotFoundException ex) {
        return buildNotFoundResponse(ex.getMessage());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingNotFound(BookingNotFoundException ex) {
        return buildNotFoundResponse(ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildNotFoundResponse(String message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                message,
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildConflictResponse(ex.getMessage());
    }

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingConflict(BookingConflictException ex) {
        return buildConflictResponse(ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildConflictResponse(String message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                message,
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        String detail = ex.getParameterName() + ": is required";
        return buildBadRequestResponse("Missing required parameter", List.of(detail));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyInUse(EmailAlreadyInUseException ex) {
        return buildConflictResponse(ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildBadRequestResponse(String message, List<String> errors) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                Instant.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildUnauthorizedResponse(ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildUnauthorizedResponse(String message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
