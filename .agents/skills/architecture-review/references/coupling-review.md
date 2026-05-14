# Coupling Review

Review for high-coupling signals that indicate components are too tightly bound
and will resist change.

Severity in this section is relative to change propagation risk:
- **High**: a single change is likely to break or require edits in multiple unrelated components
- **Medium**: increases change cost but breakage is contained
- **Low**: minor rigidity, unlikely to cause immediate harm

## Signals

| Signal | What to inspect | Observable proxy | Why it matters |
|---|---|---|---|
| Layer leakage | Business logic directly depends on DB, HTTP, file system, or framework | Domain or service classes importing ORM entities, HTTP request objects, or framework decorators directly | Infrastructure changes ripple into business logic |
| Shotgun surgery | One change requires touching many files | A single interface, constant, or type referenced across many unrelated modules | High change cost and merge conflict risk |
| Exposed internals | Public APIs expose internal persistence or domain objects | API response types that are ORM entities or internal models with no DTO layer | Internal refactors break external contracts |
| Circular dependencies | A → B → C → A | Trace import chains for cycles across modules or packages | Prevents independent testing and deployment |
| Knowledge duplication | Same rule encoded in several places | Identical or near-identical validation, calculation, or mapping logic in multiple files | Rule changes require multi-site edits with risk of divergence |
| Greedy constructors | Too many collaborators in constructors or methods | Constructors with more than 4 injected dependencies | Indicates the class has too many responsibilities and is hard to test |
| Concrete coupling | Direct instantiation of volatile dependencies | `new` keyword used on infrastructure classes (HTTP clients, repositories, email senders) inside domain or service code | Cannot substitute implementations without modifying callers |
| Missing abstraction | No port or interface where boundary volatility is high | Direct calls to third-party SDKs or infrastructure with no wrapping interface | Vendor or technology changes require widespread edits |
| High fan-in hotspots | Many callers depend on an unstable module | Count import references to a module across the codebase; flag modules with high fan-in and low stability | Changes to the depended-on module break many callers |

If no high-coupling signals are found, state:
`Coupling review: no significant issues identified for this scope.`

## Output Format (Option 3)

```markdown
# Coupling Review

Short section summary (2 to 4 sentences): what was inspected for coupling, where dependency risks are concentrated, and overall coupling posture.

## Findings
| # | From | To | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|---|

## Dependency Hotspots
| Component | Fan-in | Volatility | Risk reason | Finding refs |
|---|---|---|---|---|

## Prioritized Recommendations
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example Output (partial)

```markdown
# Coupling Review

Main coupling summary: boundary leakage is concentrated in service classes that depend directly on infrastructure and transport details.

## Findings
| # | From | To | Issue | Observable evidence | Severity | Confidence |
|---|---|---|---|---|---|---|
| 1 | `src/services/OrderService.ts` | `src/db/entities/OrderEntity.ts` | Layer leakage: domain service depends directly on ORM entity | `import { OrderEntity } from '../db/entities/OrderEntity'` at line 3 | High | High |
| 2 | `src/services/OrderService.ts` | `src/http/Request.ts` | Layer leakage: service depends on HTTP transport object | `import { Request } from '../http/Request'` at line 5 | High | High |
| 3 | `src/services/PaymentService.ts` | `stripe-sdk` | Concrete coupling: Stripe SDK called directly with no wrapping interface | `new Stripe(key)` at line 22 with no abstraction layer | High | High |
| 4 | `src/models/UserModel.ts` | — | High fan-in: 14 modules import this model directly | `grep -r UserModel src` returns 14 files across auth, billing, reporting, and admin | Medium | High |

## Dependency Hotspots
| Component | Fan-in | Volatility | Risk reason | Finding refs |
|---|---|---|---|---|
| `UserModel` | 14 | High (frequent schema changes) | Any field rename or type change breaks 14 callers | 4 |
| `OrderService` | 3 | High (business rules evolving) | Leaks into both DB and HTTP layers simultaneously | 1, 2 |

## Prioritized Recommendations
1. Introduce a `OrderDTO` to decouple `OrderService` from the ORM entity — safe first step with no logic change required.
2. Wrap the Stripe SDK behind a `PaymentGateway` interface — enables testing and future provider substitution.
3. Audit `UserModel` callers and introduce a stable read model or projection to reduce direct fan-in.
```
