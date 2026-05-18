# ADR-005: PostgreSQL as Primary Database

## Status
Accepted

## Context

ClinicFlow requires:

- Relational consistency
- Scheduling
- Transactions
- Reporting
- Multi-tenant filtering

## Decision

Use PostgreSQL as the primary database.

Reasons:

- Strong ACID guarantees
- Excellent relational modeling
- Mature ecosystem
- JSON support when needed

Redis will be used only for caching and ephemeral state.

## Consequences

### Positive

- Strong transactional guarantees
- Easier reporting
- Reliable scheduling logic

### Negative

- More relational modeling effort