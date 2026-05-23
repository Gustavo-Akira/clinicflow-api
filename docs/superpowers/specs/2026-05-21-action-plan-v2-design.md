# Action Plan v2 Design

## Objective

Redesign `docs/plans/action-plan-v1.md` into an AI-first execution plan that remains easy for humans to review. The primary goal is efficient implementation by agents. Human readability matters, but only after work units are explicit, bounded, and verifiable.

## Context

The current plan is strong as a roadmap, but weak as an execution queue for AI:

- phases are too broad for one-session implementation
- dependencies are mostly implicit
- exit criteria are qualitative
- repeated "Specifications" bullets do not define a standard artifact set
- core MVP work is mixed with later capabilities such as CRM and analytics

The repository is API-only. The MVP should optimize for the shortest useful business flow:

`tenant onboarding -> provider setup -> patient creation -> appointment booking -> async notification boundary`

Real third-party notification delivery is deferred.

## Design Principles

- Keep roadmap phases for human review.
- Execute work through small numbered slices.
- Size each slice for one agent session and one reviewable PR.
- Make dependencies explicit.
- Require verification evidence for every slice.
- Introduce cross-cutting concerns only when they unblock the next slice.
- Defer non-critical capabilities instead of mixing them into the core path.

## Document Structure

`action-plan-v2.md` should have two planning layers.

### 1. Roadmap Phases

Phases stay in the document for orientation, sequencing, and stakeholder review. Each phase should state:

- objective
- why it exists now
- business risk removed

### 2. Executable Slices

Each phase should contain numbered slices such as `2.1`, `2.2`, and `5.3`. Slices are the real unit of execution.

Each slice must use the same schema:

- `Goal`
- `Why now`
- `Depends on`
- `Tasks`
- `Artifacts`
- `Verification`
- `Done when`
- `Unlocks`

## Recommended Roadmap

### Phase 0 - Foundations

Create the baseline vocabulary and constraints required before coding:

- domain glossary completion
- ADR baseline completion
- module boundaries
- API standards
- slice authoring standards

### Phase 1 - Bootstrap Platform

Create the minimum runnable technical base:

- Gradle multi-module setup
- Spring Boot bootstrap
- PostgreSQL integration
- Flyway
- Docker local runtime
- health checks
- OpenAPI
- base error handling

### Phase 2 - Identity and Tenant Base

Establish secure tenancy and authentication:

- auth module skeleton
- login and refresh flow
- role model
- tenant context resolution
- tenant isolation enforcement base

### Phase 3 - Provider and Availability Base

Model provider operations needed for scheduling:

- provider CRUD
- specialties
- recurring availability
- blocked periods
- availability invariants

### Phase 4 - Patient Base

Add the minimum patient model needed for booking:

- patient creation
- patient profile
- minimal patient history only if required by scheduling

### Phase 5 - Appointment Scheduling Core

Deliver the primary business capability:

- slot query
- booking
- reschedule
- cancel
- confirm
- state transitions
- business invariants

### Phase 6 - Async Notification Boundary

Create the asynchronous seam without real provider integration:

- domain event publication
- outbox or async dispatch abstraction
- fake notification adapter
- reminder trigger model

### Phase 7 - Production Readiness for First Customer

Add only the reliability work needed for initial real-world use:

- CI/CD
- secrets handling
- backups
- metrics
- tracing
- idempotency
- rate limiting

### Phase 8 - Deferred Capabilities

Move non-core work out of the main implementation path:

- CRM / lead management
- analytics
- real WhatsApp integration
- complex billing

## Global Slice Policy

The plan should define rules that constrain agent behavior:

- no slice is complete without verification evidence
- no phase is complete while required slices are still open
- if a slice depends on an undefined architectural decision, create or update the ADR first
- if a slice becomes too large, split it before implementation
- do not introduce infrastructure before a slice requires it
- do not add abstractions without an immediate consumer
- optional capabilities belong in deferred scope, not active slices
- each slice must name the module it changes
- each slice must declare its execution bias: `contract-first`, `domain-first`, or `integration-first`

## Verification Model

Each slice should define verification in four layers when applicable:

### Static Verification

- formatting
- compilation
- contract generation

### Automated Verification

- unit tests
- integration tests
- module-specific commands

### Behavior Verification

- explicit API scenarios
- expected status codes
- expected persistence or side effects

### Operational Verification

- container boot
- migration success
- health endpoint
- logs, traces, or metrics when relevant

The `Done when` section must be binary and testable. Example:

- migration runs on a clean database
- `POST /auth/login` returns JWT for valid credentials
- invalid credentials return the defined error response
- OpenAPI includes endpoint and schemas
- auth tests pass

## Example Slice Template

```md
### Slice 5.2 - Book appointment

Goal:
Create the first booking command and persistence flow.

Why now:
Depends on provider availability and patient base being in place.

Depends on:
- Slice 3.3 provider availability
- Slice 4.1 patient creation
- ADR-003
- ADR-004

Tasks:
- define booking request/response contract
- implement booking application service
- enforce no-overlap invariant
- persist appointment and emit domain event
- add integration tests

Artifacts:
- OpenAPI contract
- appointment command handler
- migration
- integration tests

Verification:
- `./gradlew test --tests "*Appointment*"`
- booking valid slot returns success
- overlapping booking is rejected
- tenant boundary is enforced

Done when:
- appointment is persisted
- overlap rule is enforced
- OpenAPI is updated
- tests pass

Unlocks:
- Slice 5.3 reschedule appointment
- Slice 6.1 notification event handling
```

## Rewrite Rules for action-plan-v2

The rewrite should:

- preserve the repository's roadmap intent
- replace broad deliverable lists with executable slices
- make the core validation flow the ordering spine
- move CRM and analytics after the scheduling core
- replace vague exit criteria with binary completion checks
- standardize artifact expectations across all slices

## Out of Scope

This design does not define the full content of every slice yet. It defines the structure, sequencing model, and execution contract that `action-plan-v2.md` must follow.
