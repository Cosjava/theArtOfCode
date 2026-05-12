---
name: functional-programming
description: "Review or refactor code to improve purity and functional style where appropriate. Use when the user asks for functional programming, immutability, pure functions, side-effect isolation, or replacing imperative transformations with clear map/filter/reduce style pipelines."
---

# Functional Programming Skill

Use this skill to review or refactor code toward functional programming when it improves clarity, simplicity, and predictability.

## Step 1 - Apply functional criteria

Review code and identify opportunities to improve purity using functional programming where appropriate.

Favor a functional style when it makes the code clearer, simpler, and more predictable.
Look for places where imperative logic can be replaced by pure functions, immutable data, non-mutating operations, or readable transformation pipelines.

## Step 2 - Analyze the code

Identify:

- functions that modify external state
- functions that depend on mutable shared state
- hidden side effects (logging, database writes, HTTP calls, time-based behavior)
- imperative transformations that could be expressed as pure functions
- mutable collections that could be replaced by immutable data or non-mutating operations
- loops that could become clear and readable map, filter, reduce, or collect operations
- pipelines that are too long or too dense to remain readable

## Step 3 - Refactor with restraint

- Prefer pure functions and immutable transformations when they improve readability.
- Keep imperative code when behavior depends on visible side effects, mutable state, or exception handling that would become harder to understand inside a pipeline.
- Preserve behavior and error handling.

## Step 4 - Explain decisions

Always explain:

- which parts were made more functional and why
- which parts remain imperative and why
- which parts remain impure and why

## Default behavior

If the user does not explicitly ask for review only, refactor the code instead of returning analysis only.
