# ADR-003: API First with OpenAPI

## Status
Accepted

## Context

ClinicFlow requires synchronization between frontend and backend teams.

Strong API contracts reduce regressions and integration issues.

## Decision

All APIs must follow an **API First** approach.

Implementation order:

1. Business rules
2. OpenAPI specification
3. Backend implementation
4. TypeScript SDK generation
5. Frontend integration

The API contract becomes the source of truth.

## Consequences

### Positive

- Strong typing
- Reduced frontend/backend mismatch
- Easier documentation
- Better developer experience

### Negative

- Slightly slower initial development
- Requires discipline before coding