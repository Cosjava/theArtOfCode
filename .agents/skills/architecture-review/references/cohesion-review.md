# Cohesion Review

Review for low-cohesion signals that indicate a class or module has more than one
reason to change.

Severity in this section is relative to change risk:
- **High**: likely to cause bugs or require multi-file changes on next modification
- **Medium**: slows development or increases review burden
- **Low**: minor clarity issue, unlikely to cause immediate harm

## Signals

| Signal | What to inspect | Observable proxy | Why it matters |
|---|---|---|---|
| Responsibility scatter | One class handles unrelated concerns | Method names that span distinct domain concepts (e.g. `sendEmail` and `calculateTax` in the same class) | Multiple reasons to change |
| Field isolation | State used by only small subsets of methods | Fields referenced in fewer than half the methods of a class | Hidden subtypes inside one class |
| Mixed abstraction levels | High-level orchestration mixed with low-level details | Methods at different call depths in the same class (e.g. `processOrder` next to `buildSqlWhereClause`) | Hard to reason and change safely |
| Import breadth | Imports from many unrelated modules | Count distinct top-level import namespaces per file; flag outliers | Poor responsibility focus |
| Test setup sprawl | Many mocks or fixtures needed for a small behaviour | Test files requiring more than 3 unrelated mocks for a single method under test | Unit is not actually a unit |

If no low-cohesion signals are found, state:
`Cohesion review: no significant issues identified for this scope.`

## Output Format (Option 2)

```markdown
# Cohesion Review

Short section summary (2 to 4 sentences): what was inspected for cohesion, where low-cohesion risks are concentrated, and overall cohesion posture.

## Findings
| # | Location | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|

## Cohesion Hotspots
| Class / Module | Primary signal | Risk reason | Finding refs |
|---|---|---|---|

## Prioritized Recommendations
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example

```markdown
# Cohesion Review

Main cohesion summary: `OrderService` is the primary cohesion hotspot due to mixed responsibilities and isolated fields.

## Findings
| # | Location | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|
| 1 | `src/services/OrderService.ts` | Responsibility scatter: mixes order lifecycle, email, and payment concerns | Methods `validateOrder`, `sendConfirmationEmail`, `chargeCard` in one class | High | High |
| 2 | `src/services/OrderService.ts` | Field isolation: `smtpConfig` used only by `sendConfirmationEmail` | Field referenced in 1 of 12 methods | Medium | High |
| 3 | `src/utils/Helpers.ts` | Import breadth: imports from 7 unrelated top-level modules | `import` statements span auth, billing, logging, storage, email, validation, and reporting | Medium | Medium |

## Cohesion Hotspots
| Class / Module | Primary signal | Risk reason | Finding refs |
|---|---|---|---|
| `OrderService` | Responsibility scatter + field isolation | Will require changes for unrelated reasons (email config, payment provider, order rules) | 1, 2 |
| `Helpers.ts` | Import breadth | Likely a catch-all module with no stable reason to exist | 3 |

## Prioritized Recommendations
1. Extract email notification into a dedicated `NotificationService` — lowest risk split, no domain logic involved.
2. Extract payment logic into a `PaymentService` — higher value but requires care around transaction boundaries.
3. Audit `Helpers.ts` and redistribute methods to their natural owner modules.
```
