# Design Principles and Patterns Review

Review for principle adherence and pattern application. Recommend patterns only
when they solve a concrete observed change or coupling problem, never for ceremony.

For each principle or pattern, mark one of:
- **Applied correctly**: evidence supports correct use
- **Partially applied**: applied in some places but inconsistently
- **Violated**: clear evidence of breach
- **Missing but warranted**: absent and would solve a concrete visible problem
- **Not applicable**: genuinely irrelevant to this scope

Severity in this section is relative to design drift and future change cost:
- **High**: violation is actively increasing coupling or reducing extensibility today
- **Medium**: partial application creates inconsistency that will compound over time
- **Low**: minor drift, unlikely to cause immediate harm

## SOLID Checks

| Principle | What to detect | Observable proxy |
|---|---|---|
| **S - Single Responsibility** | Class has more than one reason to change | Methods that span distinct domain concepts; class name that requires "and" to describe |
| **O - Open/Closed** | New behavior requires modifying existing code rather than extending it | Large switch/if-else chains on type or state; no strategy or extension point where variation exists |
| **L - Liskov Substitution** | Subtype breaks the contract of its parent | Overridden methods that throw, return null, or narrow the contract; type checks on subtypes before calling methods |
| **I - Interface Segregation** | Clients depend on methods they do not use | Interfaces with many methods where callers only use a small subset; fat interfaces shared across unrelated consumers |
| **D - Dependency Inversion** | High-level policy depends on concrete low-level detail | Domain or service classes importing infrastructure directly with no intervening interface or port |

## Other Principle Checks

| Principle | What to detect | Observable proxy |
|---|---|---|
| **DRY** | Same rule or logic encoded in multiple places | Identical or near-identical validation, calculation, or mapping blocks across files |
| **Tell, Don't Ask** | Objects expose state to be acted upon externally | Chains of getters followed by conditional logic in the caller rather than behavior on the object |
| **Law of Demeter** | Classes reach through collaborators to call their collaborators | Method chains longer than two hops (e.g. `a.getB().getC().doThing()`) |
| **Composition over Inheritance** | Deep inheritance hierarchies used where composition would suffice | Abstract base classes with many subclasses; behavior changes require subclassing rather than injecting a collaborator |
| **Stable Dependencies** | Volatile modules depended upon by stable ones | Stable core modules importing frequently-changing peripheral modules |
| **Acyclic Dependencies** | Cycles between packages or modules | Import chains that loop back to the originating module |

## Pattern Review

### How to detect pattern presence

| Pattern | Detection approach |
|---|---|
| Repository | Classes named `*Repository` or `*Store` with CRUD-style methods isolating persistence from domain |
| Factory / Builder | Dedicated creation classes or methods that centralize object construction logic |
| Strategy | Interface or abstract type with multiple implementations injected at runtime for variable behavior |
| Observer / Event | Event bus, dispatcher, or listener registration decoupling emitter from consumer |
| Facade | Single entry-point class simplifying access to a complex subsystem |
| Adapter | Wrapper class translating one interface to another at a boundary |
| CQRS | Separate read and write models or handlers; infer from command/query naming conventions |
| Ports and Adapters | Domain interfaces (ports) with infrastructure implementations (adapters) injected from outside |

### When to recommend a missing pattern

Only recommend a pattern when all three conditions are met:
1. A concrete coupling or change problem is visible in the findings
2. The pattern directly resolves that problem
3. The added complexity is proportionate to the codebase size and team context

If this lens does not apply to the scope, state:
`Design principles and patterns review: not applicable for this scope.`

## Output Format (Option 4)

```markdown
# Design Principles and Patterns Review

Short section summary (2 to 4 sentences): what was inspected for principles/patterns, where violations or drift concentrate, and overall design posture.

## SOLID and Principle Status
| Principle | Status | Observable evidence | Severity |
|---|---|---|---|

## Pattern Status
| Pattern | Status | Observable evidence | Recommendation |
|---|---|---|---|

## Findings
| # | Location | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|

## Prioritized Recommendations
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example Output (partial)

```markdown
# Design Principles and Patterns Review

Main design summary: Strategy and sealed-type patterns are applied well, while Dependency Inversion and extension pressure remain concentrated in service hotspots.

## SOLID and Principle Status
| Principle | Status | Observable evidence | Severity |
|---|---|---|---|
| S - Single Responsibility | Partially applied | `OrderService` has three distinct responsibilities; most other classes are focused | Medium |
| O - Open/Closed | Violated | `OrderProcessor.ts` line 44: 12-branch switch on order type with no extension point | High |
| L - Liskov Substitution | Applied correctly | No contract-breaking overrides detected | - |
| I - Interface Segregation | Partially applied | `IUserService` interface has 9 methods; `AdminController` uses 2 | Medium |
| D - Dependency Inversion | Violated | `PaymentService` imports `StripeClient` directly; `OrderService` imports `OrderEntity` ORM class | High |
| DRY | Violated | Address validation logic duplicated in `UserService.ts` and `CheckoutService.ts` | Medium |
| Law of Demeter | Violated | `order.getCustomer().getAddress().getPostcode()` in `ShippingService.ts` line 67 | Low |

## Pattern Status
| Pattern | Status | Observable evidence | Recommendation |
|---|---|---|---|
| Repository | Missing but warranted | Persistence logic embedded directly in `OrderService` and `UserService` | Introduce `OrderRepository` and `UserRepository` to isolate persistence from domain logic |
| Strategy | Missing but warranted | 12-branch type switch in `OrderProcessor.ts` | Replace with a `OrderProcessingStrategy` interface with per-type implementations |
| Facade | Not applicable | No complex subsystem requiring simplification detected | - |

## Findings
| # | Location | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|
| 1 | `src/services/OrderProcessor.ts:44` | O violation: 12-branch type switch requires modification for every new order type | Switch statement with no extension point or strategy injection | High | High |
| 2 | `src/services/PaymentService.ts:22` | D violation: domain service depends directly on Stripe SDK concretion | `import { Stripe } from 'stripe'` with `new Stripe(key)` in service constructor | High | High |

## Prioritized Recommendations
1. Introduce a `PaymentGateway` interface and move Stripe-specific code behind an adapter; resolves D violation and enables testing without live credentials.
2. Replace the order type switch in `OrderProcessor` with a `Strategy` pattern; resolves O violation and removes the need to modify core processing logic for new order types.
3. Extract persistence calls from `OrderService` and `UserService` into dedicated Repository classes; resolves D violation and isolates ORM changes from domain logic.
```
