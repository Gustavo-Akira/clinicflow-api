# ADR-006: Asynchronous Events for Side Effects

## Status
Accepted

## Context

Operations such as:

- notifications
- reminders
- analytics
- follow-up messages

should not block user requests.

## Decision

Use domain events for asynchronous side effects.

Examples:

AppointmentCreated
AppointmentCancelled
LeadConverted

Initial implementation:

Spring Application Events

Future evolution:

Kafka or event broker.

## Consequences

### Positive

- Better response times
- Loose coupling
- Easier scalability

### Negative

- Increased debugging complexity