# CourtFlow

[🇧🇷 Português](README.pt-br.md)

A sports court booking system built to explore concurrency control, event-driven expiration, and Clean Architecture in a real, production-style backend — built as a portfolio project for Java backend internship/junior roles.

CourtFlow lets users book sports courts (tennis, futsal, etc.) while guaranteeing that two people can never successfully book the same court for overlapping time slots — even under concurrent requests — and automatically expires unconfirmed bookings after a TTL window via a message queue, without any manual cleanup job.

## Why this project

Most CRUD portfolio projects don't touch the parts of backend engineering that actually break in production: race conditions, stale reads, and cleanup of abandoned state. CourtFlow was built specifically to practice those problems with real tools (optimistic locking, RabbitMQ dead-letter queues) rather than simulating them.

## Features

- **Court management** — CRUD for sports courts, admin-only writes.
- **Booking flow** — create (with conflict detection), confirm, cancel, fetch by id, list by court.
- **Concurrency safety** — optimistic locking (`@Version`) prevents double-booking under simultaneous requests; verified with a dedicated concurrency test using `ExecutorService` + `CountDownLatch`.
- **Automatic expiration** — `PENDING` bookings that are never confirmed automatically transition to `EXPIRED` via a RabbitMQ TTL + dead-letter queue, no cron job or polling involved.
- **JWT authentication & role-based authorization** — registration, login, and explicit `hasRole`/`hasAnyRole` rules per endpoint (never a generic `.authenticated()`).
- **Centralized, consistent error handling** — every error response follows a single `ApiErrorResponse` shape.
- **Observability** — Spring Boot Actuator with a public `/health` endpoint and an admin-only `/metrics` endpoint; optional structured (ECS/JSON) logging via a dedicated Spring profile.

## Tech stack

| Layer | Technology |
|---|---|
| Language / Runtime | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Architecture | Clean Architecture (`domain` / `application` / `infrastructure`) |
| Persistence | Spring Data JPA, optimistic locking (`@Version`) |
| Messaging | RabbitMQ (TTL + dead-letter queue for booking expiration) |
| Security | Spring Security, JWT (`jjwt` 0.13.0) |
| Testing | JUnit 5, concurrency tests with `ExecutorService`/`CountDownLatch` |
| Observability | Spring Boot Actuator, ECS-formatted structured logging (optional profile) |
| Build | Maven |

## Architecture

CourtFlow follows Clean Architecture, with the domain layer having **zero** framework dependencies (no Spring, no Hibernate):

```
domain/
  model/       → pure entities and value objects (Court, Booking, TimeSlot, User)
  exception/   → business exceptions

application/
  <entity>/    → one use case per operation (CreateBookingUseCase, ConfirmBookingUseCase, ...)

infrastructure/
  persistence/ → JPA entities and Spring Data repositories
  web/         → DTOs, mappers, controllers, centralized exception handling
  security/    → JWT service, authentication filter, entry point, access denied handler
  config/      → Security and RabbitMQ configuration
  messaging/   → booking expiration listener (consumes the dead-letter queue)
```

`Booking` is modeled as an explicit state machine (`create()` / `reconstruct()` factories, `confirm()` / `cancel()` / `expire()` transitions), each method validating that the transition is legal.

## Getting started

### Prerequisites

- Java 21
- A running RabbitMQ instance
- - A running PostgreSQL instance (adjust connection settings in `application.properties`)

### Setup

```bash
git clone https://github.com/Rangeldev73/courtflow.git
cd courtflow
# configure your datasource and RabbitMQ connection settings
./mvnw spring-boot:run
```

### Optional: structured (JSON/ECS) logging

By default, CourtFlow logs in plain, human-readable text — kept as the default specifically so the local "clone and run" experience stays readable for anyone evaluating the project. To switch console output to ECS-formatted JSON (the format a log aggregation tool would consume), activate the `structured-logs` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=structured-logs
```

or, if running the built jar:

```bash
java -jar courtflow.jar --spring.profiles.active=structured-logs
```

## ⚠️ Manual step required: bootstrapping the first ADMIN

There is no public endpoint to create the first `ADMIN` user — this is intentional. Since `Court` write operations are `ADMIN`-only, allowing self-service admin registration would mean anyone could grant themselves elevated privileges, which defeats the purpose of the role restriction.

The first `ADMIN` must be promoted manually via direct SQL after registering a normal user:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

Any subsequent admin management can be done through the application once at least one `ADMIN` account exists.

## Testing

```bash
./mvnw test
```

Concurrency safety is verified by a dedicated test that fires simultaneous booking requests for the same time slot and asserts that only one succeeds.

> **Note:** Integration tests originally targeted a Testcontainers-based setup, but this is currently blocked by a Docker Desktop (Windows) issue unrelated to the project's code — the Testcontainers Docker client receives empty/stub responses from Docker Desktop 4.78 over both named pipe and exposed TCP daemon. Tests currently run via manually configured environment variables in the IDE run configuration instead. Tracked as an open item, not a blocker for the rest of the project.

## Known limitations / open items

- Log entries do not yet include structured contextual fields (e.g. `courtId`, `bookingId`) via MDC — searchable fields today are limited to what ECS provides by default (level, logger, timestamp, message). Tracked as a GitHub issue.
- Testcontainers-based integration testing is currently blocked by a local Docker Desktop issue (see Testing section above).
