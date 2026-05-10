---
name: sustainability
description: Apply chapter 8 sustainability patterns (Lean Packaging Lean Storage, Lean Communication, Efficient Execution, Memory Efficiency) to review or improve software efficiency by reducing dependency bloat, dead build artifacts, storage waste, avoidable network traffic, unnecessary CPU load, and excess memory retention; use when asked to optimize package size, dependency trees, retention policies, endpoint design, payload size, caching, polling behavior, static asset transfer, algorithm/backend efficiency, workload smoothing, cache sizing, or memory footprint.
---

# Sustainability Patterns

Use this skill to review or design improvements for chapter 8 sustainability patterns:
- Lean Packaging
- Lean Storage
- Lean Communication
- Efficient Execution
- Memory Efficiency

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
3. Propose the smallest safe change that preserves behavior.
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
- Maven dependency plugin.
- DepClean.
- `jdeps`.
- Webpack tree shaking.
- Visual Studio "Remove Unused References" (.NET).

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
- Choose polling vs push based on real update cadence and connection cost.
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
- Consider native execution options (for example AOT/native image) when startup and runtime overhead are significant and operational constraints allow.

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
- Treat gradual heap growth and rising GC frequency as signals to investigate retention issues.

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
