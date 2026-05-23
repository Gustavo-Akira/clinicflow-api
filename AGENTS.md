# Repository Guidelines

## Project Structure & Module Organization

This repository currently stores project documentation and architecture decisions under [`docs/`](./docs):

- `docs/adrs/`: architecture decision records, named `adr-###-topic.md`
- `docs/diagrams/`: PlantUML source files and exported images
- `docs/plans/`: delivery and implementation plans
- `docs/domain-glossary.md`: shared business terminology

The current plan targets a Gradle-based modular monolith backend. When source code is added, keep modules aligned with the documented domain boundaries (`auth`, `tenant`, `catalog`, `appointment`, `crm`, `notification`, `billing`, `analytics`) and preserve clear `domain`, `application`, `infrastructure`, and `presentation` layers.

## Build, Test, and Development Commands

There is no buildable application checked in yet, so no project-wide build or test commands exist today.

Use these documentation commands when relevant:

- `rg --files docs`: list tracked documentation files quickly
- `git log --pretty=format:"%h %s"`: review commit history and naming patterns

When the backend is bootstrapped, document the canonical Gradle commands here, for example `./gradlew test` and `./gradlew build`.

## Coding Style & Naming Conventions

Follow the existing documentation style:

- Use Markdown with short sections and descriptive headings.
- Keep ADRs in the form `adr-###-kebab-case-topic.md`.
- Keep diagram sources and exported images paired by feature name, for example `usecases_patient.puml` and `images/usecases_patient.png`.
- Prefer ASCII text unless the file already requires another encoding.

For future code modules, keep names domain-oriented and avoid cross-module leakage that bypasses published interfaces.

## Testing Guidelines

Documentation changes should be reviewed for internal consistency:

- terminology matches `docs/domain-glossary.md`
- new architectural rules are reflected in the relevant ADRs
- updated diagrams include regenerated image outputs when needed

Add automated test instructions here once the application code and test framework are introduced.

## Commit & Pull Request Guidelines

The current history uses concise conventional-style messages such as `docs: add usecase diagrams` and `docs: adding domain glossary and adrs`. Keep that pattern: `<scope>: <imperative summary>`.

Pull requests should include:

- a short problem statement
- the affected paths under `docs/`
- linked issue or plan item when applicable
- updated screenshots or exported diagram images for diagram changes
