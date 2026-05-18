# ADR-004: Domain-Oriented Module Boundaries

## Status
Accepted

## Context

The system contains multiple business capabilities.

Poor separation increases coupling.

## Decision

Modules must follow business boundaries.

Initial modules:

- auth
- tenant
- catalog
- appointment
- crm
- notification
- billing
- analytics

Each module should contain:

- domain
- application
- infrastructure
- presentation

Cross-module communication must happen through public interfaces.

Direct database access between modules is forbidden.

## Consequences

### Positive

- Better maintainability
- Easier testing
- Future service extraction possible

### Negative

- Requires additional abstraction