---
name: sustainability
description: Perform a green software / sustainability audit of code or architecture using five patterns from Chapter 8 (Lean Packaging, Lean Storage, Lean Communication, Efficient Execution, Memory Efficiency). Trigger on explicit requests for a sustainability audit, green review, or efficiency audit. Do NOT trigger on general architecture reviews, code quality reviews, or refactoring requests — use the architecture-review skill for those.
---

# Sustainability Patterns

Use this skill to review sustainability risks and propose improvements based on the green patterns in Chapter 8 of *The Art of Code*:
- Lean Packaging
- Lean Storage
- Lean Communication
- Efficient Execution
- Memory Efficiency

## Mode

This skill is review-only. Produce findings and recommendations; do not modify code, configuration, infrastructure, or data through this workflow.

## Scope

Focus on practical efficiency issues that waste:
- CPU
- memory
- storage
- network
- package size
- hardware pressure

## Workflow

1. Identify whether the request is about packaging, storage, communication, execution efficiency, memory efficiency, or a mix.
2. Detect concrete waste signals.
3. Propose the smallest safe improvement recommendation that preserves behavior.
4. State severity (high/medium/low) based on the expected impact of the waste in production and the improvement from the change.
5. Evaluate trade-offs.

## Lean Packaging checks

Look for:
- Unused direct or transitive dependencies.
- Large libraries used for tiny helper usage.
- Dead code or obsolete feature-flag paths.
- Test/dev assets included in production artifacts.
- Missing minification/compression/tree-shaking where relevant.

Suggested tools:
- Maven dependency plugin (for Java applications).
- DepClean (for Java applications).
- `jdeps` (for Java applications).
- Webpack tree shaking (for JavaScript/TypeScript applications).
- Visual Studio "Remove Unused References" (.NET).
- `depcheck` or `knip` (for Node.js projects).
- `pip-audit` / `pipdeptree` (for Python projects).
- `cargo udeps` (for Rust projects).
- For other ecosystems, apply the equivalent dependency analysis tool for your package manager and flag any package whose removal would not break the build or test suite.

## Lean Storage checks

Look for:
- Oversized field types or unrealistic column widths.
- Duplicate data that should be normalized.
- Large datasets stored in inefficient formats.
- Logs/temp files/backups/queues that grow without purge.
- Missing retention policies for personal data.

Policy constraints:
- Respect legal retention obligations.
- Apply privacy rules (for example GDPR/CCPA) where applicable.
- Prefer "delete when no longer needed unless law requires retention."

## Lean Communication checks

Look for:
- Too many endpoint calls for one user flow.
- N+1 client-server calls that can be batched.
- Polling frequency that is too aggressive for update frequency.
- Missing caching at client/CDN/server/data layers.
- Payloads carrying fields not needed by the caller.
- Heavy formats where lighter formats are suitable.
- Missing compression for large text payloads.
- Static assets that are too heavy or eagerly loaded.

Design heuristics:
- Prefer one well-shaped call over multiple calls when data belongs to one screen action.
- Use cache tiers with explicit expiry policies.
- Use offline-first and batched sync when interaction is bursty.
- Use push for high-cadence or latency-sensitive updates; use polling for low-cadence updates where persistent connections cost more than periodic checks.
- Return only needed fields (dedicated endpoints or selective query mechanisms).
- Use lazy loading and right-sized images/resources for device/network conditions.

## Efficient Execution checks

Look for:
- Inefficient algorithms or data structures that increase CPU time.
- DB inefficiencies (missing indexes, N+1 patterns, excessive full scans).
- Missing connection pooling.
- Workloads that spike rather than being queued/scheduled/batched.
- Synchronous heavy user-triggered tasks that could run async.
- Over-precise computation where lower precision is acceptable.
- Execution modes with avoidable runtime overhead for the use-case.

Design heuristics:
- Improve algorithmic complexity before scaling infrastructure.
- Use targeted microbenchmarks to compare alternatives before changing implementation.
- Tune data access paths (indexes/query shape) and reduce round trips.
- Use queueing, batching, and scheduling to smooth load over time.
- Apply race-to-idle principle: avoid underutilized tiny batches; finish efficiently then return to low-power state.
- For Java applications, consider native execution options (for example AOT/native image via GraalVM) when startup and runtime overhead are significant and operational constraints allow.

## Memory Efficiency checks

Look for:
- Long-lived objects retained without business need.
- Large datasets loaded fully in memory instead of streamed/paged/chunked processing.
- Caches without max size or eviction policy.
- Static collections that only grow.
- OS resources (files/sockets/streams) not closed promptly.
- Excess allocations from heavy/boxed data structures when lighter alternatives are valid.

Design heuristics:
- Process large workloads in chunks/batches/iterators/cursors to cap peak memory.
- Acquire resources late and release them early with language-safe cleanup constructs.
- Bound every cache and define explicit eviction strategy.
- Prefer lightweight structures and primitive representations when nullability is not required.
- For Java applications, treat gradual heap growth and rising GC frequency as signals to investigate retention issues.

## Output format

For each finding, include:
- Pattern: `Lean Packaging`, `Lean Storage`, `Lean Communication`, `Efficient Execution`, or `Memory Efficiency`
- Issue
- Wasted resource(s)
- Why it matters in production
- Smallest safe improvement
- Trade-off
- Severity: high/medium/low
- Confidence: high/medium/low
- Assumptions (required if confidence is medium/low)

When no meaningful issue exists, explicitly say no optimization is recommended and explain why.

## Finding Priority

When multiple findings exist, order them as follows:
1. Severity (high → low)
2. Confidence (high → low)
3. Estimated fix effort (smallest → largest)

State the ordering rationale briefly at the top of the findings section so the reader understands why item 1 appears first.

## Example

```markdown
- **Pattern:** Lean Communication
- **Issue:** Product listing page makes one HTTP call per item to fetch its thumbnail URL, resulting in N+1 requests on page load.
- **Wasted resource(s):** Network bandwidth, server CPU, client battery.
- **Why it matters in production:** A page with 50 items generates 51 requests. At moderate traffic this creates unnecessary server load and degrades mobile performance.
- **Smallest safe improvement:** Batch thumbnail URLs into the existing product list endpoint response so one call returns all data needed for the page.
- **Trade-off:** Slightly larger initial payload; eliminates 50 round trips per page load.
- **Severity:** High
- **Confidence:** High
```