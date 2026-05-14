# Client-Use Readiness (Usability and Documentation)

Review from the perspective of a team trying to adopt and operate the system.

Severity in this section is relative to adoption and operational risk:
- **High**: blocks adoption or causes production incidents
- **Medium**: slows adoption or increases support burden
- **Low**: minor friction, unlikely to block but worth fixing

## Signals

| Signal | What to inspect | Observable proxy | Why it matters |
|---|---|---|---|
| Onboarding friction | Missing prerequisites, unclear setup order, brittle bootstrap | README, CONTRIBUTING, docker-compose or equivalent, env.example presence and completeness | Slow adoption and high support load |
| Workflow discoverability | Core workflows hard to find from docs, UI, or API surface | Docs index, example folder, API reference completeness | Feature value is hard to realize |
| Operational clarity | Missing deploy, rollback, migration, backup, recovery guidance | Presence of runbooks, deployment docs, migration guides | Higher production risk |
| API/doc mismatch | Docs/examples differ from real behavior and errors | Cross-check documented endpoints, params, and error codes against actual implementation | Integration failures and trust loss |
| Error guidance quality | Errors lack actionable next steps | Inspect error messages and exception types for codes, context, and resolution hints | Debug burden shifts to users/support |
| Configuration ergonomics | Unclear or conflicting environment/config options | Check env.example, config schema, and inline comments for completeness and consistency | Frequent misconfiguration incidents |

If this lens does not apply to the scope, state:
`Client-use readiness review: not applicable for this scope.`

## Output Format

```markdown
# Client-Use Readiness Review

Short section summary (2 to 4 sentences): what was inspected for adoption/operations, where user or operator friction concentrates, and overall readiness posture.

## Findings
| # | Area | Issue | Observable evidence | User/Operator Impact | Severity | Confidence |
|---|---|---|---|---|---|---|

## Quick Wins
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example
```markdown
# Client-Use Readiness Review

Main readiness summary: onboarding and operational clarity are the dominant adoption risks for this scope.

## Findings
| # | Area | Issue | Observable evidence | User/Operator Impact | Severity | Confidence |
|---|---|---|---|---|---|---|
| 1 | Onboarding friction | No env.example or README setup section | `.env` references in code but no template or documentation found | New developers cannot bootstrap without asking the team | High | High |
| 2 | Error guidance quality | Exceptions thrown as raw strings with no error codes | `throw new Error("something went wrong")` in `src/api/orders.ts:42` | Integrators cannot programmatically handle errors | High | High |
| 3 | Configuration ergonomics | Two conflicting timeout settings with no explanation | `REQUEST_TIMEOUT` in `config.js` and `TIMEOUT_MS` in `env.example` differ | Risk of silent misconfiguration in production | Low | Medium |

## Quick Wins
1. Add an `env.example` file with every required variable and a one-line comment per entry.
2. Replace raw string throws with typed error classes that include an error code and resolution hint.
```
