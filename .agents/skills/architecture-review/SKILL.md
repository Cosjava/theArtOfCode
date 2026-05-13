---
name: architecture-review
description: Deep architecture review for cohesion, coupling, design patterns, and SOLID principles.
  Use this skill whenever the user asks to review, audit, or evaluate a codebase, module,
  package, or system for architectural quality — including mentions of "coupling", "cohesion",
  "design patterns", "SOLID", "architecture review", "code smell", "refactoring advice",
  "dependency analysis", or "architectural report". Also trigger when the user shares code
  and asks "is this well-designed?" or "what's wrong with this structure?". Do NOT rewrite
  code unless explicitly asked.
---

# Architecture Review Skill

A structured, interview-driven skill for reviewing code for **low cohesion**, **high coupling**,
violations of **design principles** (SOLID, DRY, SRP, etc.), and misapplied or missing
**design patterns**. Produces a written architectural report at the end.
 
---

## Phase 0 — Complexity Triage

Before any exploration, classify the scope:

| Tier | Description | Review depth |
|---|---|---|
| **Tiny** | Single file or < 200 LOC | Skip interview, 1–3 findings max, no full report |
| **Module** | A package or feature slice | Max 2 interview questions, condensed report |
| **System** | Multiple services or layers | Full skill protocol |

State the detected tier at the start of your response. Adjust finding count,
interview length, and report sections to match.

---

## Phase 1 — Codebase Exploration Before Any Questions

**Before asking the user anything**, fully explore what is already knowable from the code:

1. List all files, modules, and packages.
2. Map public interfaces, class hierarchies, dependency imports, and constructor signatures.
3. Identify layer boundaries (presentation / application / domain / infrastructure) from naming and structure.
4. Detect framework and persistence technology from imports and config files.
5. Infer responsibility from class/method names, file groupings, and existing comments.
6. Detect any applied design patterns (Repository, Factory, Strategy, Observer, Facade, etc.).
7. Identify SOLID violations that are already visible (e.g. god classes, mixed layers, concrete dependencies).
8. Check for the presence and coverage of tests. Note untested modules explicitly — they constrain which recommendations are safe to make.
9. If git history is accessible, check commit frequency on high-coupling classes. Coupling risk is proportional to change rate — a stable untouched class with high coupling is low priority; the same class touched every sprint is urgent.
10. Note any TODO comments, issue markers (`// FIXME`, `// HACK`), or documentation that indicates known pain points or expected future changes.

---

## Phase 2 — Targeted Interview Protocol

**Only ask the user about things you genuinely cannot infer.** When you do ask, follow the
interview protocol below.
Ask one question at a time. For each question:

- **Why it matters**: one sentence on how the answer affects the cohesion/coupling verdict.
- **Your best guess**: what the code suggests the answer is.
- **Confirmation request**: ask the user to confirm or correct.
  If a question can be answered by exploring the codebase, inspect the codebase instead of asking.
Typical remaining questions:

| Topic | Only ask if… |
|---|---|
| Module responsibility | Names and structure are ambiguous |
| Allowed layer dependencies | No layering convention is detectable |
| Stable vs volatile dependencies | Change history is unavailable |
| Public model ownership | Models are used both internally and in API responses |
| Expected future changes | No roadmap or TODO comments exist |
| Known pain points | No issue markers (`// FIXME`, `// HACK`) found |
| External contract vs internal model | DTO/entity separation is absent or unclear |
 
---

## Phase 3 — Architecture Characterisation

Before issuing findings, produce a short architecture summary:

```
## Architecture Summary
- **Style**: e.g. Layered / Hexagonal / Modular monolith / Microservice
- **Layers detected**: e.g. Controller → Service → Repository → Entity
- **Design patterns in use**: list any identified (e.g. Repository, Strategy, Facade)
- **SOLID alignment**: brief per-principle note (S / O / L / I / D)
- **Primary cohesion risk areas**: modules or classes with unclear responsibility
- **Primary coupling risk areas**: direct cross-layer or cross-concern dependencies
```

If the user requests diagrams, or if the dependency structure is complex enough that a diagram
would materially aid understanding, produce Mermaid diagrams at this phase:

- **Dependency graph**: `graph TD` showing layer and module dependencies, highlighting
  problematic edges in red with `style NodeName fill:#f66`
- **Coupling hotspots**: nodes with high fan-in or fan-out visually distinguished
- **Sequence diagram**: `sequenceDiagram` for key flows if layer leakage is detected

Only produce diagrams that add information not already clear from the summary text.

---

## Phase 4 — Cohesion Review

Look for these signals, ranked by severity:

| Signal | What to look for | Why it indicates low cohesion |
|---|---|---|
| **Responsibility scatter** | Methods that touch unrelated domain concepts (e.g. `UserService` handling emails *and* billing) | The class serves multiple masters |
| **Field isolation** | Fields used by only one or two methods, ignored by the rest | The class is really 2+ classes glued together |
| **Mixed abstraction levels** | Some methods are high-level orchestration, others are low-level implementation detail | No single stable reason to exist |
| **Import breadth** | The class imports from many unrelated modules or layers | Cohesion usually correlates with a narrow import surface |
| **Test setup complexity** | Requires many unrelated mocks or fixtures to test a single method | The unit is not actually a unit |
 
---

## Phase 5 — Coupling Review

Look for these signals:

| Signal | What to look for |
|---|---|
| **Layer leakage** | Business logic depending directly on DB, HTTP, file system, or framework |
| **Shotgun surgery** | A change in one class requires changes in many others |
| **Exposed internals** | Public APIs returning database entities or internal models |
| **Circular dependencies** | A → B → C → A |
| **Knowledge duplication** | Same concept encoded in multiple places |
| **Greedy constructors** | Constructors or methods with many collaborators |
| **Concrete coupling** | Direct `new` instantiation of volatile dependencies instead of injection |
| **Missing abstraction** | No interface or port between domain and infrastructure when one is warranted |
| **High fan-in** | A coupled module is called from many places — coupling risk scales with the number of callers, not just the coupling itself |

---

## Phase 6 — Design Patterns & Principles Assessment

For each relevant principle or pattern, state: *applied correctly / partially / violated / missing but warranted*.

### SOLID Principles

| Principle | What to check |
|---|---|
| **S — Single Responsibility** | Each class has one reason to change |
| **O — Open/Closed** | Extension via composition/inheritance, not modification |
| **L — Liskov Substitution** | Subtypes honour the contract of their parent |
| **I — Interface Segregation** | Clients depend only on the methods they use |
| **D — Dependency Inversion** | High-level modules depend on abstractions, not concretions |

### Other Principles

| Principle | What to check |
|---|---|
| **DRY** | Single source of truth; no duplicated business rules |
| **Tell, Don't Ask** | Objects act on data rather than exposing state to be acted upon |
| **Law of Demeter** | Classes talk only to immediate collaborators |
| **Composition over Inheritance** | Behaviour assembled from small units, not deep hierarchies |
| **Stable Dependencies** | Depend in the direction of stability |
| **Acyclic Dependencies** | No cycles between packages or modules |

### Design Patterns

Check for patterns that are present (correctly or incorrectly applied) and patterns that are
**missing but would resolve a concrete coupling or change problem**:

- **Creational**: Factory, Builder, Singleton — are objects created in the right place?
- **Structural**: Facade, Adapter, Decorator, Composite — are interfaces clean at boundaries?
- **Behavioural**: Strategy, Observer, Command, Template Method — is variability managed with patterns or with conditionals?
- **Architectural**: Repository, CQRS, Event-driven, Ports & Adapters (Hexagonal) — does the macro structure match the access patterns?
> **Rule**: Only recommend a pattern if it solves a concrete, visible dependency or change problem.
> Do not recommend patterns for ceremony.
 
---

## Phase 7 — Finding Format

For each finding, output a structured block:

```
### Finding N — [Short Title]
 
- **Type**: Low cohesion | High coupling | Design principle violation | Missing pattern
- **Location**: file / class / method
- **Issue**: what is wrong
- **Architectural assumption**: the principle or pattern being violated
- **Why it harms durability**: concrete consequence (e.g. "adding a new persistence store requires changes in the domain layer")
- **Smallest safe improvement**: the minimal change that improves the situation
- **Trade-off**: what is lost in simplicity or readability if the fix is applied
- **Test safety**: safe to change (tests exist) | risky (no tests) | unknown
- **Severity**: high | medium | low
- **Confidence**: high | medium | low
- **Assumptions** *(if confidence < high)*: what you assumed and why
```
 
---

## Phase 8 — Architecture Report

After all findings, produce a report using only the sections that have substance.
Omit any section where the honest content would be "N/A" or "none identified".
For Tiny-tier reviews, replace the full report with a single findings summary table
plus one short recommendations paragraph.

```markdown
# Architecture Review Report
**Module / Package**: …
**Date**: …
 
---
 
## 1. Executive Summary
One paragraph. Overall health, most critical risk, recommended first action.
 
## 2. Architecture Overview
- Style and layers
- Design patterns in use
- SOLID alignment snapshot (one line per principle)
 
## 3. Cohesion Analysis
Narrative + finding references. Include a cohesion heat-map table if > 5 classes:
 
| Class / Module | Responsibility clarity | Finding refs |
|---|---|---|
 
## 4. Coupling Analysis
Narrative + finding references. Include a dependency direction table if helpful:
 
| From | To | Type | Finding refs |
|---|---|---|---|
 
## 5. Design Patterns & Principles
For each applicable principle/pattern: status + finding reference.
 
## 6. Findings Summary
 
| # | Type | Location | Severity | Confidence |
|---|---|---|---|---|
 
## 7. Prioritised Recommendations
Ordered by impact/effort. Prefer incremental, safe steps.
 
1. …
2. …
 
## 8. Stability Forecast
Which parts of the code are most likely to hurt the team in the next 3–6 months if left unchanged?
 
## 9. Scope Note
This is a review only. No code has been changed. Changes should be made incrementally with test coverage.
```
 
---

## Behaviour Rules

- **Exit early on systemic failure.** If findings exceed 15 or violations are present in every layer, stop issuing individual findings. Instead write the top 10 findings by severity, then add a warning that the issues shown are not exhaustive and that the codebase likely requires a broader remediation strategy.
- **No rewrites** unless the user explicitly asks for one.
- **No mechanical SOLID application.** Apply principles only where they solve a real problem, and express the solution through a concrete design pattern rather than abstract principle alone.
- **No over-abstraction.** An interface is only recommended when it decouples a concrete, volatile dependency.
- **Prefer naming clarity** as a first fix before structural refactoring.
- **Frame findings structurally, never personally.** Findings describe code, not developers. Write "this class mixes responsibilities" not "this was implemented incorrectly". The report should be safe to share with the team without editing.
- **Related skills — out of scope, handoff only.** This skill does not review error handling or code readability. If the review incidentally surfaces poor error handling or missing failure paths, note at the end: *"error handling was not reviewed — consider running the failure-handling skill."* If naming or readability issues appear frequently, note: *"code readability was not reviewed — consider running the narrative-code skill."*
- **Offer to save the report.** At the end of the review, ask the user if they would like the report saved as `docs/architecture-review-YYYY-MM-DD.md` relative to the project root. Create the `docs` folder if it does not exist.