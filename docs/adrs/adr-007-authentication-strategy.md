# ADR-007: JWT-Based Authentication

## Status
Accepted

## Context

ClinicFlow requires secure multi-tenant authentication.

The system must support:

- Admin
- Receptionist
- Professional

## Decision

Authentication will use JWT access tokens.

Authorization will be role-based.

Roles:

- TENANT_ADMIN
- RECEPTIONIST
- PROFESSIONAL

Tenant context will be embedded in claims.

## Consequences

### Positive

- Stateless authentication
- Horizontal scalability

### Negative

- Token invalidation complexity