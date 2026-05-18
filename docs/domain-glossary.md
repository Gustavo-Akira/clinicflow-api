# Domain Glossary (Ubiquitous Language)

This glossary defines the canonical language used across product, engineering, design, API specifications, and documentation.

The goal is to maintain a **shared and unambiguous vocabulary**. Avoid synonyms and prefer the terms defined here in code, OpenAPI descriptions, ADRs, and discussions.

---

# Business Domain Terms

## Tenant

A customer organization (clinic or business) that owns data isolated within the platform.

Examples:

- Dental clinic
- Aesthetic clinic
- Local healthcare business

Tenant-owned data must always belong to a single tenant.

---

## tenant_id

The identifier used to scope tenant-owned data.

Used to guarantee tenant isolation in a shared database strategy.

---

## Patient

A person receiving care or services from a tenant.

A patient may:

- book appointments
- receive communications
- have appointment history
- interact with one or more providers

A patient belongs to a single tenant.

---

## Lead

A prospective patient who has shown interest in a service but has not yet completed an appointment.

Examples:

- WhatsApp inquiry
- Landing page conversion
- Form submission

A lead may later become a patient.

---

## Provider

A healthcare professional capable of delivering one or more services and participating in appointments.

Examples:

- Dentist
- Hygienist
- Specialist

A provider may have:

- specialties
- working hours
- availability rules

---

## Service

A business offering provided by a tenant and available for booking.

Examples:

- Cleaning
- Teeth Whitening
- Consultation
- Implant Evaluation

A service may define:

- duration
- provider eligibility
- pricing
- booking rules

---

## Availability

Recurring or specific periods in which a provider is available to accept appointments.

Examples:

- Monday to Friday, 09:00–18:00
- Vacation block
- Lunch break

Availability is used to determine bookable time.

---

## Slot

A bookable time interval derived from provider availability, scheduling rules, and existing appointments.

A slot is considered available only if booking constraints are satisfied.

Example:

```txt
10:00–10:30
14:30–15:00