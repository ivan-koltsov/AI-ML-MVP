<!--
Sync Impact Report
==================
Version change: (none) → 1.0.0
Modified principles: N/A (initial creation)
Added sections: All (Core Principles, Technology Stack, Quality Gates, Governance)
Removed sections: N/A
Templates requiring updates:
  - .specify/templates/plan-template.md ✅ (Constitution Check references constitution)
  - .specify/templates/spec-template.md ✅ (no mandatory section changes)
  - .specify/templates/tasks-template.md ✅ (task categorization compatible)
Follow-up TODOs: None
-->

# AI-ML-MVP Constitution

## Core Principles

### I. FinTech Data Integrity
Sensitive data (chats, user info) MUST be handled with auditability in mind. All data mutations and AI outputs affecting user decisions MUST be traceable. No silent failures; errors propagate with context. Rationale: FinTech regulatory and trust requirements demand explicability.

### II. Module-First
Features start as standalone, testable modules. Backend services and frontend components MUST be independently verifiable. Libraries MUST have clear purpose—no organizational-only artifacts. Rationale: Enables incremental delivery and parallel development.

### III. API Contract First
Backend–frontend boundaries MUST be defined in contracts (OpenAPI, TypeScript types) before implementation. Breaking contract changes require version bump and migration path. Rationale: Java/Spring + React/TypeScript split requires explicit interfaces.

### IV. Testing Discipline
New features MUST have corresponding tests for core paths. Contract changes require contract tests. Integration tests for cross-boundary flows (API ↔ DB, API ↔ ML). Tests are optional per spec when not requested, but when requested they are mandatory. Rationale: MVP velocity with guardrails; FinTech stability needs.

### V. Observability
Structured logging (JSON or key-value) for operational events. Chat sessions and ML inference calls SHOULD be loggable with request IDs. No hard requirement for full tracing initially—start simple, add when needed. Rationale: Debuggability and future compliance tooling.

### VI. Simplicity
YAGNI: avoid speculative abstractions. Start with minimal viable structure; refactor when patterns emerge. Complexity MUST be justified in plan.md when constitution principles are violated. Rationale: MVP focus; avoid over-engineering before product-market fit.

## Technology Stack

- **Backend**: Java, Spring Boot, Spring AI
- **Frontend**: React, TypeScript
- **Storage**: MongoDB
- **Domain**: Enterprise FinTech chatbot with ML features

Constitution does not mandate specific versions; those belong in implementation plan and project config.

## Quality Gates

- Specs MUST include user scenarios with acceptance criteria (mandatory).
- Plans MUST include Constitution Check before Phase 0 research.
- Tasks MUST be organized by user story for independent MVP slices.
- Contract changes MUST be reflected in tasks/contracts.

## Governance

Constitution supersedes ad-hoc practices. Amendments require:
1. Update to `.specify/memory/constitution.md`
2. Version bump per semantic rules (MAJOR/MINOR/PATCH)
3. Sync Impact Report prepended to constitution
4. Propagation to plan-template, spec-template, tasks-template if affected

All PRs and spec reviews MUST verify compliance. Deferred items use `TODO(<FIELD>): explanation` and appear in Sync Impact Report.

**Version**: 1.0.0 | **Ratified**: 2025-02-18 | **Last Amended**: 2025-02-18
