# ADR-001: Modular Monolith Architecture

## Status
Accepted

## Context

ClinicFlow is an early-stage multi-tenant SaaS platform for clinics and local businesses.

The product requires:

- Fast iteration
- Low operational complexity
- Strong domain separation
- Future scalability
- Simple deployments

Microservices introduce additional operational complexity such as:

- Distributed transactions
- Service orchestration
- Increased infrastructure cost
- Complex local development

## Decision

The backend architecture will follow a **Modular Monolith** approach.

The application will be deployed as a single service while maintaining clear module boundaries.

Modules must communicate internally through explicit application services and contracts.

Example modules:

- auth
- tenant
- appointment
- crm
- notification
- analytics
- billing

## Consequences

### Positive

- Faster development speed
- Easier debugging
- Simpler deployment
- Better local developer experience
- Lower infrastructure cost

### Negative

- Requires discipline to avoid tight coupling
- Future extraction into microservices may require refactoring

## Enforcement

To ensure the architectural style is preserved and to avoid unintended coupling, apply the following measures:

- **Contracts:** All modules must expose functionality through application services and DTOs; direct access to a module's internal state is prohibited.
- **Dependency rules:** Enforce automatic checks (architectural lint) in CI to block unauthorized inter-module imports.
- **Owners and reviews:** Each module must have a designated owner and PR review rules that validate domain boundaries.
- **Tests:** Maintain module-level integration tests to validate internal contracts and prevent coupling regressions.
- **Observability:** Instrument modules (metrics, tracing, logs) to locate incidents and implicit dependencies.
- **Documentation:** Define and publish bounded contexts and responsibilities in a design document for each module.
- **Controlled evolution:** Extract modules into independent services only when metrics or operational needs indicate it.

These actions represent minimum protections; add automated checks and stricter validations as the project matures.


## Future Evolution

Modules may be extracted into standalone services if scaling or organizational complexity demands it.
