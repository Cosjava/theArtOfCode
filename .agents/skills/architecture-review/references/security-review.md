# Cybersecurity Failure Review

Review for architecture-level security failures. This review covers structural
and design-level risks only — it is not a penetration test or a full code audit.

Severity in this section is relative to breach impact and exploitability:
- **High**: exploitable path with direct impact on confidentiality, integrity, or availability
- **Medium**: increases attack surface or degrades defence-in-depth
- **Low**: minor hardening gap, unlikely to be exploited in isolation

## CVE and OWASP Lookup

Before starting the review, ask the user:
*"This review includes CVE verification for dependencies and OWASP Top 10 lookup.
May I access the following sources during the review?*
- *https://nvd.nist.gov — CVE database for dependency vulnerabilities*
- *https://owasp.org/Top10 — current OWASP Top 10 categories and guidance*"*

Wait for explicit confirmation before proceeding.

Rules:
- If confirmed: for each lookup, state the exact URL being accessed before fetching
  it (e.g. `Accessing: https://nvd.nist.gov/vuln/search/results?query=jsonwebtoken+8.5.1`).
- If confirmed: fetch https://owasp.org/Top10 before issuing any findings and use
  the live categories retrieved — do not rely on training data for OWASP mappings
  as the Top 10 is updated periodically.
- If confirmed: keep a deduplicated list of every external security URL actually
  accessed so it can be emitted later in `## External Security References Used`.
- If declined or unavailable: state `CVE lookup skipped — verify manually at
  https://nvd.nist.gov and https://owasp.org/Top10` and proceed without CVE data.
- If declined or unavailable: fall back to the cached categories below and note
  that these may not reflect the current published list.
- Do not fabricate CVE identifiers. If a CVE cannot be confirmed via the sources
  above, note the risk pattern only.
- Map each finding to the most relevant OWASP Top 10 category regardless of whether
  internet access was confirmed.

Fallback OWASP Top 10 categories (use only when internet access is unavailable):
- A01 Broken Access Control
- A02 Cryptographic Failures
- A03 Injection
- A04 Insecure Design
- A05 Security Misconfiguration
- A06 Vulnerable and Outdated Components
- A07 Identification and Authentication Failures
- A08 Software and Data Integrity Failures
- A09 Security Logging and Monitoring Failures
- A10 Server-Side Request Forgery

## Signals

| Signal | What to inspect | Observable proxy | Why it is risky |
|---|---|---|---|
| Trust boundary leakage | Untrusted input crosses layers without validation boundary | Trace user-controlled input from entry point to persistence or processing without a validation or sanitisation step | Injection and data integrity failures |
| AuthN/AuthZ ambiguity | Access control is scattered or UI-only | Authorization checks only in controllers or middleware with no enforcement in service or domain layer | Policy bypass via alternate paths |
| Secret handling risk | Secrets in code, default config, or broad propagation | Hardcoded strings matching key/token/password patterns; secrets passed through constructors or environment without a secrets manager | High credential compromise impact |
| Insecure defaults | Permissive CORS, debug exposure, weak session or TLS defaults | CORS set to `*`; debug flags enabled in non-dev config; session cookies missing `Secure` or `HttpOnly`; TLS version not enforced | Production vulnerability by misconfiguration |
| Boundaryless side effects | File, network, or process operations without guardrails | `exec`, `spawn`, `fs.write`, or outbound HTTP calls with no input sanitisation or allowlist | RCE, exfiltration, lateral movement risk |
| Dependency attack surface | Stale or high-risk dependencies on privileged boundaries | Dependencies with known CVEs; packages last updated more than 12 months ago on critical paths | Supply-chain compromise paths |
| Auditability gaps | Missing or low-context security event logs | No logging on authentication events, permission failures, or data access on sensitive resources | Weak detection and incident response |

If this lens does not apply to the scope, state:
`Cybersecurity failure review: not applicable for this scope.`

## Output Format 

```markdown
# Cybersecurity Failure Review

Short section summary (2 to 4 sentences): what was inspected for architecture-level security, where threat exposure concentrates, and overall security posture.

## Threat Findings
| # | Entry Point | Missing Control | Impacted Asset | CIA Impact | OWASP | Severity | Confidence |
|---|---|---|---|---|---|---|---|

## CVE Findings
| Dependency | Version | CVE | Severity | Remediation |
|---|---|---|---|---|

## Top Threat Paths
- Entry point → missing control → impacted asset

## Prioritized Mitigations
1. ...
2. ...

_External references are emitted once in the final report section `## External Security References Used`._
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example Output (partial)

```markdown
# Security Review

Main security summary: threat exposure is concentrated in boundary handling and exception/logging behavior, with dependency CVEs called out explicitly when confirmed.

## Threat Findings
| # | Entry Point | Missing Control | Impacted Asset | CIA Impact | OWASP | Severity | Confidence |
|---|---|---|---|---|---|---|---|
| 1 | `POST /orders` request body | No input validation before `OrderRepository.findByFilter()` | Database — all order records | C: High, I: High, A: Medium | A03 Injection | High | High |
| 2 | `config/default.js` line 12 | JWT secret hardcoded as `"dev-secret"` | All authenticated sessions | C: High, I: High, A: Low | A02 Cryptographic Failures | High | High |
| 3 | `app.js` line 4 | CORS set to `*` with no allowlist | All API endpoints | C: Medium, I: Low, A: Low | A05 Security Misconfiguration | Medium | High |

## CVE Findings
| Dependency | Version | CVE | Severity | Remediation |
|---|---|---|---|---|
| `jsonwebtoken` | 8.5.1 | CVE-2022-23529 | High | Upgrade to 9.0.0 or later |

## Top Threat Paths
- `POST /orders` → no validation → `OrderRepository.findByFilter()` → database full read/write
- `config/default.js` hardcoded secret → forged JWT → full authentication bypass

## Prioritized Mitigations
1. Add an input validation boundary (schema validation) at the controller layer before any repository call.
2. Move JWT secret to environment variable and enforce its presence at startup.
3. Replace `CORS *` with an explicit allowlist in `app.js`.

_External references are emitted once in the final report section `## External Security References Used`._
```
