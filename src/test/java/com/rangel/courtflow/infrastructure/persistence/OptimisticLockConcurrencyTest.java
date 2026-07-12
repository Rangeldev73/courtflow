package com.rangel.courtflow.infrastructure.persistence;

import com.rangel.courtflow.domain.model.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ActiveProfiles("test")
class OptimisticLockConcurrencyTest {

    @Autowired
    private BookingJpaRepository bookingJpaRepository;

    @Test
    void shouldPreventConcurrentUpdatesOnSameBooking() throws InterruptedException {
        UUID bookingId = UUID.randomUUID();
        BookingJpaEntity initialBooking = new BookingJpaEntity();
        initialBooking.setId(bookingId);
        initialBooking.setCourtId(UUID.randomUUID());
        initialBooking.setCustomerId(UUID.randomUUID());
        initialBooking.setStatus(BookingStatus.PENDING);
        initialBooking.setStartTime(LocalDateTime.of(2026, 7, 10, 18, 0));
        initialBooking.setEndTime(LocalDateTime.of(2026, 7, 10, 19, 0));

        bookingJpaRepository.save(initialBooking);

        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Runnable task = () -> {
            readyLatch.countDown();
            try {
                startLatch.await();

                BookingJpaEntity booking = bookingJpaRepository.findById(bookingId)
                        .orElseThrow(() -> new IllegalStateException("Booking not found"));

                booking.setStatus(BookingStatus.CONFIRMED);
                bookingJpaRepository.save(booking);

                successCount.incrementAndGet();
            } catch (ObjectOptimisticLockingFailureException e) {
                failureCount.incrementAndGet();
            } catch (Exception e) {
                fail("Unexpected exception caught inside concurrent thread: " + e.getMessage());
            }
        };

        executorService.submit(task);
        executorService.submit(task);

        readyLatch.await();
        startLatch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(1, successCount.get(), "Exactly one transaction should be successful");
        assertEquals(1, failureCount.get(), "Exactly one transaction should fail due to concurrency");

        BookingJpaEntity finalBooking = bookingJpaRepository.findById(bookingId).orElseThrow();
        assertEquals(1L, finalBooking.getVersion(), "The version should have incremented exactly once");
    }
}