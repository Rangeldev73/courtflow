package com.rangel.courtflow.infrastructure.messaging;

import com.rangel.courtflow.application.booking.ExpireBookingUseCase;
import com.rangel.courtflow.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingExpirationListener {

    private final ExpireBookingUseCase expireBookingUseCase;

    public BookingExpirationListener(ExpireBookingUseCase expireBookingUseCase) {
        this.expireBookingUseCase = expireBookingUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.EXPIRED_QUEUE)
    public void handleBookingExpiration(UUID bookingId) {
        try {
            expireBookingUseCase.execute(bookingId);
        } catch (IllegalStateException e) {}
    }
}