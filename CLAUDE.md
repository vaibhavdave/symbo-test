# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 1.5.4 REST API banking service using Java 8, MongoDB, and RabbitMQ. Artifact: `com.symbo.assignment:banking-app:0.0.1-SNAPSHOT`. App runs on port 9090 with context path `/symbo`.

## Commands

```bash
# Build and run
mvn clean install
mvn spring-boot:run

# Testing
mvn test                          # Run all tests (uses Fongo in-memory MongoDB — no external DB needed)

# Code coverage
mvn jacoco:report                 # Report in target/site/jacoco/

# SonarQube analysis
mvn sonar:sonar -Dsonar.projectKey=springboot-banking-app -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<token> -Dsonar.java.binaries=target/**

# Docker
docker-compose up                                             # App + MongoDB
docker-compose -f docker-compose-sonarqube.yml up            # SonarQube server on port 9000
```

Swagger UI: `http://localhost:9090/symbo/swagger-ui.html`

## Architecture

Layered MVC + Service pattern under `com.symbo.assignment`:

- **controller/** — REST endpoints (`TransactionController`): ping, create account, deposit, withdraw, account enquiry
- **service/** — Business logic (`TransactionService`), RabbitMQ publishing (`EventDispatcher`), VO→DTO conversion (`VOtoDTOConverter`)
- **repository/** — MongoDB access (`AccountRepository`, `TransactionRepository`); interfaces in `api/`
- **model/** — Three representations per entity:
  - `vo/` (Value Objects) — incoming REST request bodies
  - `bo/` (Business Objects) — internal/DB representation
  - `dto/` (Data Transfer Objects) — service layer intermediaries
  - `response/` — API response wrappers
  - `exchange/` — RabbitMQ event payloads
- **exception/** — `InvalidInputException`, `AccountNotFoundException`, `InsufficientBalanceException`
- **config/** — `AppConfig` (RabbitMQ + email validator), `SwaggerConfig`, `TestConfig` (Fongo for tests)

**Key flows:**
- Account numbers are auto-incremented using a MongoDB counter document (`CounterBO`)
- All deposits/withdrawals are recorded as `TransactionBO` entries in addition to updating `AccountBO` balance
- Account creation publishes an `AccountOpenedEvent` to RabbitMQ exchange `AccountExchange` with routing key `account.opened`
- Tests use Fongo (in-memory MongoDB) via `TestConfig` — no external MongoDB required for testing

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/transactionService/v1/ping` | Health check |
| POST | `/transactionService/v1/accounts` | Create account |
| POST | `/transactionService/v1/accounts/deposits` | Deposit money |
| POST | `/transactionService/v1/accounts/withdrawal` | Withdraw money |
| GET | `/transactionService/v1/accounts/enquiry/{accountNumber}` | Account details |

## MongoDB Configuration

Default URI: `mongodb://admin:admin@localhost:27017/symbo` (overridable via `MONGO_URL` env var). The Docker Compose setup creates a `symbo` database with persistent volume.
