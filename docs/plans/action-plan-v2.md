# ClinicFlow Backend Development Plan v2

## Objective

Build the first production-ready MVP of the ClinicFlow API with a plan that is efficient for AI agents to execute and easy for humans to review.

## Priorities

1. Fast iteration
2. Business validation
3. Strong domain boundaries
4. Low operational complexity
5. Future scalability

## Architecture Constraints

- Modular Monolith
- Multi-tenant
- API First
- Hexagonal Architecture
- PostgreSQL
- Docker-based deployment

## Primary Validation Flow

The MVP should optimize for the shortest useful backend flow:

`tenant onboarding -> provider setup -> patient creation -> appointment booking -> async notification boundary`

## Global Slice Policy

- A slice is the unit of execution.
- One slice should fit in one agent session and one reviewable PR.
- No slice is complete without verification evidence.
- No phase is complete while required slices remain open.
- If a slice depends on an undefined architectural decision, create or update the ADR first.
- If a slice becomes too large, split it before implementation.
- Do not introduce infrastructure before a slice requires it.
- Do not add abstractions without an immediate consumer.
- Optional capabilities belong in deferred scope, not active slices.
- Each slice must name the module it changes.
- Each slice must declare its execution bias: `contract-first`, `domain-first`, or `integration-first`.

## Verification Standard

Each slice should define, when applicable:

- `Static verification`: formatting, compilation, contract generation
- `Automated verification`: unit tests, integration tests, module-specific commands
- `Behavior verification`: API scenarios, status codes, side effects
- `Operational verification`: container boot, migration success, health checks, logs or traces

---

## Phase 0 - Foundations

**Objective:** Remove architectural ambiguity before coding starts.
**Why now:** Every later slice depends on shared terminology and boundary decisions.
**Risk removed:** Misaligned module design and unstable API conventions.

### Slice 0.1 - Lock bootstrap-critical glossary terms
- **Module:** `docs`
- **Execution bias:** `domain-first`
- **Depends on:** existing `docs/domain-glossary.md`
- **Tasks:** define only the terms required by bootstrap and identity, normalize tenant and auth vocabulary, defer non-blocking business terminology
- **Artifacts:** updated glossary
- **Verification:** bootstrap and identity ADRs use the same tenant, user, role, and module terminology
- **Done when:** no Phase 1 or Phase 2 slice depends on an undefined core term
- **Unlocks:** 0.2, 0.3

### Slice 0.2 - Confirm bootstrap-critical ADRs
- **Module:** `docs/adrs`
- **Execution bias:** `domain-first`
- **Depends on:** 0.1
- **Tasks:** confirm only the ADRs that affect bootstrap and identity: architecture style, multitenancy, API design, module boundaries, and authentication
- **Artifacts:** updated bootstrap-critical ADRs
- **Verification:** no unresolved decision remains that would change Phase 1 or Phase 2 code shape
- **Done when:** bootstrap and identity slices have clear architectural references without waiting on later concerns such as observability or file storage
- **Unlocks:** 1.1, 2.1

### Slice 0.3 - Define API and slice authoring standards
- **Module:** `docs`
- **Execution bias:** `contract-first`
- **Depends on:** 0.1, 0.2
- **Tasks:** define API versioning, error response shape, OpenAPI conventions, slice template
- **Artifacts:** standards document or plan appendix
- **Verification:** later slices can reference one canonical standard
- **Done when:** no core implementation slice needs to invent its own API conventions
- **Unlocks:** all contract-producing slices

---

## Phase 1 - Bootstrap Platform

**Objective:** Create the minimum runnable technical base.
**Why now:** Identity and scheduling work need a stable runtime and persistence foundation.
**Risk removed:** Early feature work built on an unstable project skeleton.

### Slice 1.1 - Create Gradle multi-module skeleton
- **Module:** bootstrap
- **Execution bias:** `integration-first`
- **Depends on:** 0.2
- **Tasks:** create root build, application module, initial domain modules, dependency management
- **Artifacts:** Gradle structure, module build files
- **Verification:** project compiles with empty module skeletons
- **Done when:** a clean checkout can run the base Gradle lifecycle
- **Unlocks:** 1.2, 2.1, 3.1

### Slice 1.2 - Boot Spring application with health endpoint
- **Module:** bootstrap
- **Execution bias:** `integration-first`
- **Depends on:** 1.1
- **Tasks:** wire Spring Boot app, expose health endpoint, add smoke test
- **Artifacts:** boot app, health endpoint, boot test
- **Verification:** application starts locally; health endpoint responds
- **Done when:** the service boots without feature modules implemented
- **Unlocks:** 1.3, 1.4

### Slice 1.3 - Add Dockerized PostgreSQL and Flyway base migration
- **Module:** bootstrap / persistence
- **Execution bias:** `integration-first`
- **Depends on:** 1.2
- **Tasks:** wire datasource, add first migration, create local database profile, provide Docker runtime for PostgreSQL only
- **Artifacts:** DB config, Flyway config, base migration, local database container config
- **Verification:** Dockerized PostgreSQL starts successfully; migration runs on a clean database
- **Done when:** the app connects locally to PostgreSQL with versioned schema migration
- **Unlocks:** all persistence slices

### Slice 1.4 - Add OpenAPI and base error standard
- **Module:** bootstrap / api
- **Execution bias:** `contract-first`
- **Depends on:** 1.2, 0.3
- **Tasks:** configure OpenAPI generation, define shared error response pattern, add request validation baseline
- **Artifacts:** OpenAPI endpoint, shared API error model
- **Verification:** OpenAPI is exposed; invalid requests return standard error shape
- **Done when:** later feature slices can publish contracts without redefining base API behavior
- **Unlocks:** 2.2, 3.1, 4.1, 5.1

---

## Phase 2 - Identity and Tenant Base

**Objective:** Establish secure tenancy and authentication.
**Why now:** Every business flow depends on tenant isolation and role-based access.
**Risk removed:** Cross-tenant leakage and uncontrolled access.

### Slice 2.1 - Create auth and tenant module skeletons
- **Module:** `auth`, `tenant`
- **Execution bias:** `domain-first`
- **Depends on:** 1.1, 0.2
- **Tasks:** create packages, public interfaces, boot wiring, module smoke tests
- **Artifacts:** module skeletons, context-loading tests
- **Verification:** application context loads with modules enabled
- **Done when:** identity modules exist with stable internal boundaries
- **Unlocks:** 2.2, 2.4

### Slice 2.2 - Publish login contract and JWT flow
- **Module:** `auth`
- **Execution bias:** `contract-first`
- **Depends on:** 2.1, 1.4
- **Tasks:** define login request/response, implement JWT generation, invalid credential handling
- **Artifacts:** auth endpoint, token service, tests, OpenAPI contract
- **Verification:** valid login returns JWT; invalid login returns standard error response
- **Done when:** API clients can authenticate against a defined contract
- **Unlocks:** 2.3

### Slice 2.3 - Add refresh token flow and role model
- **Module:** `auth`
- **Execution bias:** `contract-first`
- **Depends on:** 2.2
- **Tasks:** define roles, implement refresh endpoint, validate token lifecycle
- **Artifacts:** refresh contract, role definitions, tests
- **Verification:** refresh issues a new access token; role claims are available to secured endpoints
- **Done when:** the auth model supports session continuity and authorization decisions
- **Unlocks:** secured business endpoints

### Slice 2.4 - Implement tenant context resolution
- **Module:** `tenant`
- **Execution bias:** `integration-first`
- **Depends on:** 2.1, 1.3
- **Tasks:** define tenant identification strategy, resolve tenant per request, expose tenant context
- **Artifacts:** middleware/filter, tenant context component, tests
- **Verification:** requests resolve tenant consistently and reject invalid tenant context
- **Done when:** downstream modules can consume tenant identity safely
- **Unlocks:** 2.5, all tenant-aware slices

### Slice 2.5 - Enforce tenant isolation base
- **Module:** `tenant`
- **Execution bias:** `integration-first`
- **Depends on:** 2.3, 2.4
- **Tasks:** apply tenant checks to persistence and service boundaries, add negative tests for cross-tenant access
- **Artifacts:** enforcement hooks, isolation tests
- **Verification:** cross-tenant reads and writes are rejected
- **Done when:** tenant isolation is proven by automated tests
- **Unlocks:** 3.1, 4.1, 5.1

---

## Phase 3 - Provider and Availability Base

**Objective:** Model providers and the availability needed for scheduling.
**Why now:** Booking depends on provider identity and valid time windows.
**Risk removed:** Scheduling against incomplete or invalid provider state.

### Slice 3.1 - Publish provider create/update contract
- **Module:** `provider`
- **Execution bias:** `contract-first`
- **Depends on:** 1.4, 2.5
- **Tasks:** define provider payloads, create and update endpoints, validation rules
- **Artifacts:** provider API, tests, OpenAPI contract
- **Verification:** provider create/update succeeds for valid input and rejects invalid input
- **Done when:** providers can be managed per tenant through the API
- **Unlocks:** 3.2

### Slice 3.2 - Add provider specialties model
- **Module:** `provider`
- **Execution bias:** `domain-first`
- **Depends on:** 3.1
- **Tasks:** model specialties, connect them to provider records, expose through API
- **Artifacts:** specialty persistence, API updates, tests
- **Verification:** provider specialty data persists and is returned through the contract
- **Done when:** scheduling can reason about provider capability metadata
- **Unlocks:** 3.3

### Slice 3.3 - Implement recurring availability
- **Module:** `availability`
- **Execution bias:** `domain-first`
- **Depends on:** 3.1, 2.5
- **Tasks:** model recurring schedule, persistence, API endpoints, invariants
- **Artifacts:** recurring availability model, migrations, tests
- **Verification:** valid recurring windows persist; invalid windows are rejected
- **Done when:** providers have a stable recurring schedule base
- **Unlocks:** 3.4, 5.1

### Slice 3.4 - Add blocked periods and vacations
- **Module:** `availability`
- **Execution bias:** `domain-first`
- **Depends on:** 3.3
- **Tasks:** model blocked periods, vacations, overlap rules with recurrence
- **Artifacts:** blocked period API, invariants, tests
- **Verification:** blocked periods suppress otherwise valid availability
- **Done when:** scheduling can exclude unavailable provider time correctly
- **Unlocks:** 5.1, 5.2

---

## Phase 4 - Patient Base

**Objective:** Add the minimum patient model required for booking.
**Why now:** Appointments need a valid patient identity before scheduling starts.
**Risk removed:** Booking flows built on partial or inconsistent patient records.

### Slice 4.1 - Publish patient create/profile contract
- **Module:** `patient`
- **Execution bias:** `contract-first`
- **Depends on:** 1.4, 2.5
- **Tasks:** define patient payloads, create endpoint, profile retrieval, validation rules
- **Artifacts:** patient API, persistence, tests, OpenAPI contract
- **Verification:** patient create and fetch scenarios pass per tenant
- **Done when:** a tenant can register and retrieve patients through the API
- **Unlocks:** 5.2

### Slice 4.2 - Add only patient history required by scheduling
- **Module:** `patient`
- **Execution bias:** `domain-first`
- **Depends on:** 4.1
- **Tasks:** model minimal history fields only if appointment rules need them
- **Artifacts:** optional history extension, tests
- **Verification:** only implemented if an appointment invariant requires it
- **Done when:** no booking rule depends on missing patient state
- **Unlocks:** 5.x as needed

---

## Phase 5 - Appointment Scheduling Core

**Objective:** Deliver the first high-value business capability.
**Why now:** This is the main validation target for the MVP.
**Risk removed:** Having a platform that cannot perform the clinic's core job.

### Slice 5.1 - Publish available-slot query contract
- **Module:** `appointment`
- **Execution bias:** `contract-first`
- **Depends on:** 3.3, 3.4, 2.5, 1.4
- **Tasks:** define slot query input/output, compute slots from availability and blocked periods
- **Artifacts:** slot query API, tests, OpenAPI contract
- **Verification:** returned slots exclude blocked or invalid times
- **Done when:** clients can query viable scheduling windows
- **Unlocks:** 5.2

### Slice 5.2 - Implement appointment booking
- **Module:** `appointment`
- **Execution bias:** `domain-first`
- **Depends on:** 5.1, 4.1
- **Tasks:** define booking command, enforce no-overlap and duration rules, persist appointment, emit event
- **Artifacts:** booking API, migration, command handler, tests
- **Verification:** valid booking succeeds; overlapping booking is rejected; tenant boundary is enforced
- **Done when:** an appointment can be booked end-to-end through the API
- **Unlocks:** 5.3, 6.1

### Slice 5.3 - Implement reschedule flow
- **Module:** `appointment`
- **Execution bias:** `domain-first`
- **Depends on:** 5.2
- **Tasks:** define reschedule contract, revalidate slot availability, persist state change
- **Artifacts:** reschedule API, state rules, tests
- **Verification:** valid reschedule succeeds; invalid target slot is rejected
- **Done when:** a booked appointment can move safely to another valid slot
- **Unlocks:** 5.4

### Slice 5.4 - Implement cancel flow
- **Module:** `appointment`
- **Execution bias:** `domain-first`
- **Depends on:** 5.2
- **Tasks:** define cancel contract, enforce cancellation rules, emit cancellation event
- **Artifacts:** cancel API, business rules, tests
- **Verification:** valid cancel succeeds; invalid state transitions are rejected
- **Done when:** appointments can be canceled under defined business rules
- **Unlocks:** 5.5, 6.1

### Slice 5.5 - Implement confirm flow and state transitions
- **Module:** `appointment`
- **Execution bias:** `domain-first`
- **Depends on:** 5.2, 5.4
- **Tasks:** define confirmation contract, formalize appointment lifecycle, document legal transitions
- **Artifacts:** confirm API, state model, tests, transition diagram if needed
- **Verification:** only legal state transitions are accepted
- **Done when:** appointment lifecycle rules are explicit and enforced
- **Unlocks:** 6.1, phase completion

---

## Phase 6 - Async Notification Boundary

**Objective:** Introduce asynchronous delivery boundaries without third-party coupling.
**Why now:** Scheduling should emit business events before real provider integration is added.
**Risk removed:** Hard-wiring notification vendors into the core domain too early.

### Slice 6.1 - Publish appointment domain events
- **Module:** `appointment` / `notification`
- **Execution bias:** `integration-first`
- **Depends on:** 5.2, 5.4, 5.5
- **Tasks:** define event contracts for booked, canceled, confirmed appointments
- **Artifacts:** event models, event publishing path, tests
- **Verification:** domain events are emitted on the expected transitions
- **Done when:** notification workflows can subscribe without touching appointment internals
- **Unlocks:** 6.2

### Slice 6.2 - Add outbox or async dispatch boundary
- **Module:** `notification`
- **Execution bias:** `integration-first`
- **Depends on:** 6.1, 1.3
- **Tasks:** persist dispatch intent, define dispatcher boundary, add retry-safe base behavior
- **Artifacts:** outbox/dispatch abstraction, persistence, tests
- **Verification:** scheduled dispatch records are created reliably from emitted events
- **Done when:** async delivery is decoupled from request-response flows
- **Unlocks:** 6.3

### Slice 6.3 - Add fake notification adapter and reminder triggers
- **Module:** `notification`
- **Execution bias:** `integration-first`
- **Depends on:** 6.2
- **Tasks:** implement fake adapter, define reminder trigger model, validate end-to-end event consumption
- **Artifacts:** fake sender, trigger rules, tests
- **Verification:** booking-related events reach the fake adapter asynchronously
- **Done when:** the notification boundary is proven without real provider integration
- **Unlocks:** phase completion, future real channel integration

---

## Phase 7 - Production Readiness for First Customer

**Objective:** Add only the operational features needed for initial real-world use.
**Why now:** Reliability work should follow the first validated core flow.
**Risk removed:** Shipping the MVP without the minimum operational safeguards.

### Required slices
- CI/CD pipeline baseline
- secrets and environment handling
- database backup strategy
- metrics and tracing baseline
- idempotency for sensitive write flows
- rate limiting for exposed APIs

Each of these should be split into one-session slices using the same schema once the core API flow is validated.

---

## Phase 8 - Deferred Capabilities

Do not place these in the core MVP path:

- CRM / lead management
- analytics and KPI APIs
- real WhatsApp integration
- complex billing
- Kubernetes
- Kafka
- microservices
- multi-region
- CQRS
- event sourcing
- AI receptionist

## Phase Completion Criteria

- **Phase 0 complete:** no architecture-critical ambiguity remains for bootstrap work
- **Phase 1 complete:** application boots locally, Dockerized PostgreSQL is reachable, Flyway migration runs on a clean database, health endpoint responds, OpenAPI is exposed, and invalid request payloads return the standard error shape
- **Phase 2 complete:** users authenticate successfully and tenant isolation is proven by tests
- **Phase 3 complete:** providers and availability can be managed with invariants enforced
- **Phase 4 complete:** patients required for scheduling can be created and retrieved
- **Phase 5 complete:** booking, reschedule, cancel, and confirm flows work end-to-end
- **Phase 6 complete:** appointment events are dispatched asynchronously through a fake adapter
- **Phase 7 complete:** the system is operationally ready for the first paying customer
