---
name: architecture-review
description: Deep architecture review for cohesion, coupling, design patterns, SOLID principles, cybersecurity failure exposure, client-use readiness (usability plus documentation), and git history signals. Use when asked to review architecture quality, coupling/cohesion, design patterns, SOLID alignment, security architecture risk, client adoption readiness, or architecture-relevant git history risk. Do not rewrite code unless explicitly asked.
---

# Architecture Review Skill

Review code following the Durability principles from The Art of Code, Chapter 9.

Evaluate cohesion, coupling, design principles and patterns, cybersecurity failure exposure, client-use readiness (usability plus documentation), and git history risk signals based on the selected scope.
Avoid code changes and focus on findings and recommendations.

## Mode - Review Only (required)

- Do not rewrite code.
- Exit early on systemic failure: if findings exceed 15 or issues exist in every layer, provide top 10 by severity and state the list is not exhaustive.
- If scope includes `1` or `5`, execute the live CVE/OWASP internet lookup workflow defined in `references/security-review.md` before finalizing security findings.
- Do not silently skip CVE/OWASP lookup for scope `1` or `5`; only skip if the user explicitly declines internet lookup or the sources are unavailable.

Before starting, ask the user to select the review scope and load only the relevant reference sections. 

## Step 1 - Ask Scope First (required)

Before code exploration, ask this question first:

Choose review scope (reply with numbers, separated by comma or space):
1. Full review (all sections)
2. Cohesion only
3. Coupling only
4. Design principles and patterns only
5. Security only
6. Client-use readiness only
7. Documentation quality only
8. Git history signals only

Rules:
- If the user does not choose, default to `1`.
- If the user chooses multiple numbers, run only those sections.
- Accept `,` and whitespace as separators (examples: `2,5` and `2 5`).
- If the scope is tiny (single file or less than 200 LOC), suggest one targeted option (`2` to `8`).

## Step 2 - Load References by Selection

Load only selected modules:
- Option `1` (full):
  [references/cohesion-review.md](references/cohesion-review.md),
  [references/coupling-review.md](references/coupling-review.md),
  [references/design-principles-patterns-review.md](references/design-principles-patterns-review.md),
  [references/security-review.md](references/security-review.md),
  [references/client-use-readiness-review.md](references/client-use-readiness-review.md),
  [references/documentation-quality-review.md](references/documentation-quality-review.md),
  [references/git-history-signals-review.md](references/git-history-signals-review.md)
- Option `2`: [references/cohesion-review.md](references/cohesion-review.md)
- Option `3`: [references/coupling-review.md](references/coupling-review.md)
- Option `4`: [references/design-principles-patterns-review.md](references/design-principles-patterns-review.md)
- Option `5`: [references/security-review.md](references/security-review.md)
- Option `6`: [references/client-use-readiness-review.md](references/client-use-readiness-review.md)
- Option `7`: [references/documentation-quality-review.md](references/documentation-quality-review.md)
- Option `8`: [references/git-history-signals-review.md](references/git-history-signals-review.md)

## Section 1 - Complexity Triage

Classify scope before deep review:

| Scope Level | Description | Review depth |
|---|---|---|
| Tiny | Single file or less than 200 LOC | Skip interview, 1 to 3 findings max, no multi-section report |
| Module | One package or feature slice | Max 2 interview questions, condensed report |
| System | Multiple services or layers | Full protocol |

State detected scope level at the start of the review.

## Section 2 - Codebase Exploration Before Questions

Before asking the user anything, inspect what is already knowable:

1. If scope includes `1` or `5`, run the CVE/OWASP lookup pre-check defined in `references/security-review.md` (including explicit internet lookup confirmation step when required by that reference).
2. If scope includes `1` or `8` and git history is available, count unique contributors (all-time and recent window when feasible).
3. If scope includes `1` or `8`, identify most modified files using a churn proxy (commit touches count when line-level churn is unavailable) and files with many distinct authors.
4. If scope includes `1` or `8`, identify largest files by LOC and call out overlaps with high-churn files.
5. If scope includes `1` or `8`, derive hotspot signals (for example: high churn plus large size, or high churn plus low test surface).
6. List files, modules, and packages (prioritize hotspot files for deeper checks when git signals are available).
7. Map public interfaces, class hierarchies, imports, and constructor signatures.
8. Identify layer boundaries (presentation, application, domain, infrastructure).
9. Detect framework, runtime, persistence, and transport choices.
10. Infer responsibility from names and grouping.
11. Check tests and coverage shape; call out untested modules.
12. Note TODO, FIXME, HACK, and existing pain-point comments.

## Section 3 - Targeted Interview Protocol

Ask questions only for gaps that cannot be inferred from code.

Ask one question at a time and include:
- Why it matters (one sentence)
- Best guess from code
- Confirmation request

Typical topics to ask only when unclear:
- Module responsibility
- Allowed layer dependencies
- Stable versus volatile dependencies
- API model ownership versus internal model ownership
- Expected future changes without roadmap clues
- Known pain points when code has no markers

Pause and redirect within interview:
- During review, pause when a signal may deserve deeper investigation instead of diving immediately.
- Typical pause triggers:
  - circular dependency or dependency direction anomaly
  - large high-churn file/class hotspot
  - unclear layer boundary or ownership
  - conflicting architectural signals across modules
- When triggered, ask one short question before deep dive:
  - What was observed (1 sentence)
  - Why deeper inspection may matter (1 sentence)
  - Best guess from current evidence (1 sentence)
  - Redirect choice: inspect now | capture as risk and continue | skip for this review
- Use pauses sparingly (max 3 per review unless user asks for exhaustive analysis).
- Prefer pausing on high-impact uncertainty, not minor style issues.
- If no user response is available, continue with a conservative default: capture risk and proceed.

## Section 4 - Architecture Summary and Common Header (required)

Before any scope-specific section outputs, emit one shared architecture summary header block for the whole response.

Use this format:

```markdown
# Architecture Review Report
**Scope selected**: ...
**Scope Level**: Tiny | Module | System

## Overview
- Architecture type: layered | hexagonal | modular monolith | microservice | event-driven | hybrid
- Layer style and dependency direction: ...
- System type: frontend | backend | full-stack | library | mixed
- Primary technologies: language, framework/runtime, persistence, transport
- Files reviewed: X
- Total files in scope: Y
- Approximate LOC reviewed: Z
- Test surface observed: unit | integration | e2e | none | mixed
- Notes: any constraints or relevant scope caveats
```

Rules:
- Emit this header once per report response.
- Do not repeat this header inside each scope section.
- If a field is unknown, state `unknown` and add one short assumption note.
- Diagrams are optional. Use only when structure is complex enough that text is insufficient.

## Section 5 - Git History Scope Handling

Git history output is scope-driven:
- If scope includes `1` or `8`, use `references/git-history-signals-review.md`.
- If scope includes `1` or `8`, run git-history exploration first in Section 2 and use it to prioritize deeper architectural inspection.
- If scope excludes `1` and `8`, do not emit a Git History Signals section.
- If git history is unavailable when needed, emit `Git history signals: unavailable in current environment.` and state the reason briefly.
- Keep git-based conclusions evidence-based; avoid speculative ownership claims.

## Section 6 - Output Templates by Scope

Do not collapse all scopes into one generic narrative template.
Always emit Section 4 architecture summary and common header first.
Use the output format in the selected reference:

- Option `1`: emit all scope outputs from options `2`, `3`, `4`, `5`, `6`, `7`, and `8` as separate top-level sections in one response.
- Option `2`: `references/cohesion-review.md`
- Option `3`: `references/coupling-review.md`
- Option `4`: `references/design-principles-patterns-review.md`
- Option `5`: `references/security-review.md`
- Option `6`: `references/client-use-readiness-review.md`
- Option `7`: `references/documentation-quality-review.md`
- Option `8`: `references/git-history-signals-review.md`

If multiple options are selected and `1` is included, expand to full set (`2..8`).
If multiple options are selected without `1`, emit each selected output as its own top-level section in the same response.
In each selected scope section, immediately after the section title (`# ... Review`), add a short summary paragraph (2 to 4 sentences) that explains:
- what was inspected for that section,
- where risk or quality signals are concentrated,
- and the overall posture for that section.
Keep this summary section-specific; do not repeat the global architecture header/context from Section 4.
Within each selected scope section, keep findings ordered by severity.
Include file and line references when possible across all findings and hotspot tables.

After all selected scope sections, emit a required `## Conclusion` section:
  - Top 3 cross-scope risks
  - Recommended execution order for mitigations
  - Confidence limits / missing evidence
  - Scope constraints (what was not assessed)

If scope includes `1` or `5`, after `## Conclusion`, emit a required `## External Security References Used` section:
- If lookup was performed: list deduplicated external URLs actually accessed.
- If lookup was declined/unavailable: state that clearly and include manual verification links (`https://nvd.nist.gov`, `https://owasp.org/Top10`).

- Keep out-of-scope notes explicit:
  - Error-handling depth not covered: suggest `failure-handling` skill.
  - Readability narrative style not covered: suggest `narrative-code` skill.

End the review response by asking whether to save the report as `docs/architecture-review-YYYY-MM-DD.md`.
