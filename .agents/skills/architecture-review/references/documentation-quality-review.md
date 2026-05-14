# Documentation Quality Review

Use when the user selects documentation-only scope.

Severity in this section is relative to adoption and operational risk:
- **High**: absence blocks onboarding, integration, or safe production operation
- **Medium**: gap slows teams down or increases support burden
- **Low**: minor clarity or freshness issue, unlikely to block progress

## Where to Look

Inspect documentation in this order before issuing findings:
1. `README.md` and any root-level markdown files
2. `/docs` folder and subfolders
3. Inline code comments, especially on public interfaces and configuration
4. OpenAPI, AsyncAPI, or equivalent API specification files
5. `CHANGELOG.md`, `CONTRIBUTING.md`, `SECURITY.md`
6. Package manifests (`package.json`, `pyproject.toml`, etc.) for description and script docs
7. CI/CD pipeline definitions for undocumented operational steps

Note which locations were found and which were absent.

## Signals

| Area | What to inspect | Observable proxy | Why it matters |
|---|---|---|---|
| Setup docs | Prerequisites, install steps, run order, environment variables | README has a Getting Started section; env.example exists and is annotated; steps are numbered and ordered | Teams can start without trial-and-error |
| API docs | Accuracy of request/response contracts and examples | OpenAPI spec or equivalent present; examples match actual payloads; error responses documented | Integrations succeed predictably |
| Error docs | Common failure cases and remediation guidance | Error codes or types listed with cause and resolution; not just HTTP status codes | Faster debugging and support |
| Operations docs | Deploy, rollback, migration, backup, recovery procedures | Runbook or ops guide present; rollback steps explicit; migration instructions versioned | Safe production operation |
| Versioning and changelog signals | Compatibility notes and upgrade path clarity | CHANGELOG.md present and current; breaking changes flagged; deprecation notices in code or docs | Lower upgrade risk |
| Information architecture | Findability, duplication, and stale content markers | Docs index or table of contents present; no contradictory instructions across files; last-updated markers or dates on critical docs | Lower cognitive load and drift risk |

If this lens does not apply to the scope, state:
`Documentation quality review: not applicable for this scope.`

## Output Format (Option 7)

```markdown
# Documentation Quality Review

Short section summary (2 to 4 sentences): what documentation surfaces were inspected, where gaps are concentrated, and overall documentation posture.

## Findings
| # | Doc / File | Gap | Observable evidence | Impact | Severity | Confidence |
|---|---|---|---|---|---|---|

## Priority Doc Fixes
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example Output (partial)

```markdown
# Documentation Quality Review

Main documentation summary: setup and operational documentation gaps are concentrated in missing core artifacts and stale/incomplete usage guidance.

## Findings
| # | Doc / File | Gap | Observable evidence | Impact | Severity | Confidence |
|---|---|---|---|---|---|---|
| 1 | README.md | No setup or prerequisites section | README contains project description only; no install, run, or environment variable instructions | New developers cannot bootstrap without tribal knowledge | High | High |
| 2 | — | No env.example | `.env` references found in `config/index.ts` but no template or documentation of required variables | Silent misconfiguration risk in every new environment | High | High |
| 3 | README.md | Stale API examples reference removed endpoints | `POST /api/v1/orders/submit` documented but not present in current router; current endpoint is `POST /api/v2/orders` | Integrators will hit 404s following official examples | High | High |
| 4 | — | No CHANGELOG or versioning signals | No CHANGELOG.md; no deprecation notices in code; no version history in any doc file | Upgrade risk is invisible to downstream consumers | Medium | High |

## Priority Doc Fixes
1. Add an env.example with every required variable and a one-line comment per entry.
2. Add a Getting Started section to README covering prerequisites, install steps, and run order.
3. Audit README examples against current router definitions and update or remove stale endpoints.
4. Create a CHANGELOG.md and document breaking changes since last release.
```
