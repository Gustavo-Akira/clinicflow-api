# ADR-008: Observability by Default

## Status
Accepted

## Context

Failures in scheduling and notifications directly affect revenue.

Observability is required from the beginning.

## Decision

The platform must implement:

- Structured logging
- Metrics
- Distributed tracing

Technologies:

- OpenTelemetry
- Micrometer
- Centralized logging

Business metrics should also be exposed.

Examples:

- appointment conversion rate
- cancellation rate
- no-show rate

## Consequences

### Positive

- Faster incident detection
- Better business insights

### Negative

- Slight infrastructure overhead