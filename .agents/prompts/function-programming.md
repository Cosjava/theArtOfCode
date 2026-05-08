Review this code and identify opportunities to improve purity using functional programming where it is appropriate.

Favor a functional style when it makes the code clearer, simpler, and more predictable. 
In particular, look for places where imperative logic can be replaced by pure functions, immutable data, non-mutating operations, or readable transformation pipelines.

Look for:
- functions that modify external state
- functions that depend on mutable shared state
- hidden side effects such as logging, database writes, HTTP calls, or time-based behavior
- imperative transformations that could be expressed as pure functions
- mutable collections that could be replaced by immutable data or non-mutating operations
- loops that could become clear and readable map, filter, reduce, or collect operations
- pipelines that are too long or too dense to remain readable

Refactor toward functional programming when it improves clarity, simplicity, and predictability.
Keep imperative code when side effects, exception handling, ordering, or sequencing are essential to understanding the behavior.

Do not make the code functional merely for style.

Explain:
- which parts were made more functional and why
- which parts remain imperative and why
- which parts remain impure and why