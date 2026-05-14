# Git History Signals Review

Use when the user selects git-history-focused scope, or when full review includes
git signals.

Severity in this section is relative to volatility and durability risk:
- **High**: file is large, frequently changed, and has low test surface or high coupling
- **Medium**: file shows one or two risk signals but not in combination
- **Low**: single minor signal, unlikely to cause immediate harm

## Data Collection

Collect git signals using these commands where available:

| Signal | Preferred command | Fallback |
|---|---|---|
| Line churn per file | `git log --numstat --pretty=format: | awk '{add+=$1; del+=$2} END {print add+del, FILENAME}'` | Commit touch count: `git log --format=format: --name-only | sort | uniq -c | sort -rn` |
| Distinct authors per file | `git log --format='%ae' -- <file> | sort -u | wc -l` | `git shortlog -sn -- <file>` |
| Unique contributors overall | `git log --format='%ae' | sort -u | wc -l` | `git shortlog -sn | wc -l` |
| Unique contributors recent | `git log --since="90 days ago" --format='%ae' | sort -u | wc -l` | Same with `--since` |
| File size by LOC | `wc -l **/*` or `find . -name '*.ts' | xargs wc -l | sort -rn` | IDE or file system inspection |

If git data is unavailable, state:
`Git history signals: unavailable in current environment.`
and skip all git-derived findings.

## Author Overlap Thresholds

Author overlap risk is relative to inferred team size:
- **Small team (1–5)**: 3+ distinct authors on a single file is high overlap risk
- **Medium team (6–20)**: 5+ distinct authors on a single file is high overlap risk
- **Large team (20+)**: flag files where author count exceeds 20% of total contributors

If team size cannot be inferred, use unique contributors overall as a proxy and note the assumption.

## Signals

| Area | What to inspect | Observable proxy | Why it matters |
|---|---|---|---|
| Contributor breadth | Unique contributors overall and in a recent window | Total unique commit authors; recent window (default 90 days) unique authors | Ownership concentration and bus-factor risk |
| Churn hotspots | Most modified files by churn signal | Top 10 files by line churn or commit touch count | Volatility and regression-prone areas |
| Author overlap | Distinct authors per hotspot file | Unique author count per high-churn file against team size thresholds above | Coordination and merge-friction risk |
| Size hotspots | Largest files by LOC | Top 10 files by line count; flag files over 500 LOC | Change cost and review burden |
| Combined hotspots | Large files with high churn and low test surface or high coupling | Files appearing in both churn and size top 10; cross-reference with test surface from Section 2 | Architectural durability risk |

If this lens does not apply to the scope, state:
`Git history signals review: not applicable for this scope.`

## Output Format (Option 8)

```markdown
# Git History Signals Review

Short section summary (2 to 4 sentences): what git signals were inspected, where volatility/durability risks concentrate, and overall history-risk posture.

## Data Availability
- Git history: available | unavailable
- Recent window: N days (default 90)
- Churn metric used: line churn | commit touches proxy
- Team size inferred: N contributors (basis for author overlap thresholds)

## Contributor Signals
| Metric | Value | Notes |
|---|---|---|
| Unique contributors (overall) | ... | ... |
| Unique contributors (recent window) | ... | ... |
| Bus-factor estimate | ... | Number of contributors responsible for >50% of commits |

## Churn Hotspots
| # | File | Churn Signal | Distinct Authors | Test Surface | Severity | Confidence |
|---|---|---|---|---|---|---|

## Size Hotspots
| # | File | LOC | Churn Signal | Test Surface | Severity | Confidence |
|---|---|---|---|---|---|---|

## Combined Risk Signals
| File | Size risk | Churn risk | Test surface | Coupling signal | Overall risk |
|---|---|---|---|---|---|

## Priority Actions
1. ...
2. ...
```

Section rule: immediately after the section title, add a short section summary paragraph (2 to 4 sentences). Do not add `Executive Summary` or repeated architecture context.

## Example Output (partial)

```markdown
# Git History Signals Review

Main git-history summary: risk is concentrated where high churn, large file size, and low test surface overlap.

- Git history: available
- Recent window: 90 days
- Churn metric used: line churn (added + deleted lines)
- Team size inferred: 12 contributors (basis for author overlap thresholds)

## Contributor Signals
| Metric | Value | Notes |
|---|---|---|
| Unique contributors (overall) | 12 | Active project with broad contribution |
| Unique contributors (recent window) | 4 | Ownership narrowing in last 90 days |
| Bus-factor estimate | 2 | Two contributors responsible for 61% of commits |

## Churn Hotspots
| # | File | Churn Signal | Distinct Authors | Test Surface | Severity | Confidence |
|---|---|---|---|---|---|---|
| 1 | `src/services/OrderService.ts` | 1,842 lines changed | 7 | None detected | High | High |
| 2 | `src/api/routes/orders.ts` | 934 lines changed | 4 | Partial (integration only) | Medium | High |
| 3 | `src/config/index.ts` | 612 lines changed | 6 | None detected | Medium | High |

## Size Hotspots
| # | File | LOC | Churn Signal | Test Surface | Severity | Confidence |
|---|---|---|---|---|---|---|
| 1 | `src/services/OrderService.ts` | 1,240 | High (rank 1) | None detected | High | High |
| 2 | `src/models/UserModel.ts` | 890 | Low | Partial | Low | High |

## Combined Risk Signals
| File | Size risk | Churn risk | Test surface | Coupling signal | Overall risk |
|---|---|---|---|---|---|
| `src/services/OrderService.ts` | High (1,240 LOC) | High (rank 1, 90d) | None | High fan-in (14 importers) | Critical |
| `src/config/index.ts` | Medium (420 LOC) | Medium (rank 3, 90d) | None | Imported by 22 modules | High |

## Priority Actions
1. Add unit test coverage to `OrderService.ts` before any further changes — no safe refactoring path exists without a test safety net.
2. Investigate bus-factor risk: two contributors own 61% of commits; consider knowledge-sharing sessions or pairing on high-churn files.
3. Audit `config/index.ts` — high churn on a file imported by 22 modules with no tests is a systemic fragility risk.
```
