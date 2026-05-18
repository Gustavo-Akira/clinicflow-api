# ADR-009: External File Storage

## Status
Accepted

## Context

Clinics may upload:

- logos
- images
- documents

## Decision

Files must not be stored locally.

Use object storage.

Initial provider:

AWS S3 compatible storage.

Database stores metadata only.

## Consequences

### Positive

- Better scalability
- Easier backups

### Negative

- External dependency