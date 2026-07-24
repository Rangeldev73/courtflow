package com.rangel.courtflow.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PENDING_QUEUE = "booking.pending-confirmation.queue";
    public static final String EXPIRED_QUEUE = "booking.expired.queue";
    public static final String EXPIRATION_EXCHANGE = "booking.expiration.exchange";
    public static final String EXPIRED_ROUTING_KEY = "booking.expired";

    @Bean
    public Queue pendingConfirmationQueue() {
        return QueueBuilder.durable(PENDING_QUEUE)
                .withArgument("x-dead-letter-exchange", EXPIRATION_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EXPIRED_ROUTING_KEY)
                .withArgument("x-message-ttl", 30000L)
                .build();
    }

    @Bean
    public Queue expiredQueue() {
        return QueueBuilder.durable(EXPIRED_QUEUE).build();
    }

    @Bean
    public DirectExchange expirationExchange() {
        return new DirectExchange(EXPIRATION_EXCHANGE);
    }

    @Bean
    public Binding expiredQueueBinding() {
        return BindingBuilder
                .bind(expiredQueue())
                .to(expirationExchange())
                .with(EXPIRED_ROUTING_KEY);
    }
}