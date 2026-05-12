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

## Phase 0 — Codebase Exploration Before Any Questions

**Before asking the user anything**, fully explore what is already knowable from the code:

1. List all files, modules, and packages.
2. Map public interfaces, class hierarchies, dependency imports, and constructor signatures.
3. Identify layer boundaries (presentation / application / domain / infrastructure) from naming and structure.
4. Detect framework and persistence technology from imports and config files.
5. Infer responsibility from class/method names, file groupings, and existing comments.
6. Detect any applied design patterns (Repository, Factory, Strategy, Observer, Facade, etc.).
7. Identify SOLID violations that are already visible (e.g. god classes, mixed layers, concrete dependencies).
   **Only ask the user about things you genuinely cannot infer.** When you do ask, follow the
   interview protocol below.

---

## Phase 1 — Targeted Interview Protocol

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

## Phase 2 — Architecture Characterisation

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
 
---

## Phase 3 — Cohesion Review

Look for these signals, ranked by severity:

| Signal | What to look for |
|---|---|
| **SRP violation** | Classes or modules that mix unrelated responsibilities |
| **Conceptual mismatch** | Methods in the same class that belong to different business concepts |
| **God/Manager class** | Utility or manager classes that collect unrelated behavior |
| **Field isolation** | Fields used by only a subset of methods |
| **Divergent change** | Methods that change for different, unrelated reasons |
| **Unclearable name** | Code that is hard to name clearly because it does too many things |
 
---

## Phase 4 — Coupling Review

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
 
---

## Phase 5 — Design Patterns & Principles Assessment

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

## Phase 6 — Finding Format

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
- **Severity**: high | medium | low
- **Confidence**: high | medium | low
- **Assumptions** *(if confidence < high)*: what you assumed and why
```
 
---

## Phase 7 — Architecture Report

After all findings, produce a full report using this template:

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

- **No rewrites** unless the user explicitly asks for one.
- **No mechanical SOLID application.** Apply principles only where they solve a real problem.
- **No over-abstraction.** An interface is only recommended when it decouples a concrete, volatile dependency.
- **Prefer naming clarity** as a first fix before structural refactoring.
- **One question at a time** during the interview phase.
- **Self-answer first**: inspect the codebase before asking the user.
- Confidence must be declared honestly. If an assumption is made, state it.