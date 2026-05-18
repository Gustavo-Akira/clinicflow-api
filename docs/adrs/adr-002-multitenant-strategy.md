# ADR-002: Shared Database Multi-Tenancy

## Status
Accepted

## Context

ClinicFlow serves multiple clinics (tenants).

A multi-tenant strategy is required to isolate customer data.

Possible approaches:

1. Database per tenant
2. Schema per tenant
3. Shared database with tenant_id

The system is in MVP stage.

## Decision

Use a **shared database strategy with tenant_id isolation**.

All tenant-owned entities must contain:

- tenant_id

Access to tenant data must always be filtered by tenant context.

Tenant context will be resolved from authentication.

## Consequences

### Positive

- Lower infrastructure cost
- Easier migrations
- Simpler operational maintenance
- Faster onboarding

### Negative

- Requires strong query discipline
- Risk of tenant leakage if filtering fails

## Constraints

No query should access tenant data without tenant context.

Global entities are exceptions.

Examples:

- countries
- currencies
- system configuration

## Enforcement

To reduce the risk of data leakage and ensure isolation, apply the following technical and operational protections:

- **Mandatory tenant context:** authentication middleware must resolve and inject `tenant_id` into every request; fail the request (403/500) if missing.
- **DB-level protections:** where possible, enable Row-Level Security (Postgres RLS) or use views/procedures that enforce filtering by `tenant_id`.
- **Constraints and indexes:** use composite unique constraints including `tenant_id` (e.g., `unique (tenant_id, slug)`) and include `tenant_id` in FKs when applicable.
- **ORM and centralized repositories:** centralize data access through repositories that automatically apply the tenant filter; prohibit raw queries without PR review.
- **CI checks:** integration tests that create multiple tenants to validate isolation; checks that detect queries without tenant filters in DB/ORM changes.
- **Auditing and logging:** log/audit sensitive accesses and queries with tenant context; alert on cross-tenant access.
- **Migrations and backups:** policies to avoid accidental restores that mix tenant data; review migrations for correct `tenant_id` handling.
- **Evolution plan:** document criteria to move a tenant to a separate schema/DB (e.g., compliance, storage usage, performance).

Implement middleware + repository + tests first (low cost, high benefit). Adopt RLS and stricter DB controls as the customer base and security requirements grow.