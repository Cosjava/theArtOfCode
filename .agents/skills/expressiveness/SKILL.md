---
name: expressiveness
description: "Tune code style guidance by expressiveness temperature. Use when the user asks to apply low, medium, or hot/high expressiveness, or asks for an expressiveness temperature selection workflow. The skill asks for a numeric choice (1, 2, or 3) and then applies the matching prompt profile."
---

# Expressiveness Temperature Skill

Run this workflow when expressiveness level must be selected before coding or refactoring guidance.

## Step 1 - Ask for temperature

If the user has not already chosen a level, ask:

```text
Choose expressiveness temperature:
1) low
2) medium
3) hot
```

Accept `1`, `2`, `3`, or the words `low`, `medium`, `hot` (and `high` as equivalent to `hot`).

## Step 2 - Map selection to prompt

Apply exactly one profile:

- `1` / `low` -> prompt from `references/low-expressiveness.md`
  - Use a conservative, idiomatic style.
  - Prioritize explicit constructs over expressiveness or conciseness.
  - Avoid advanced language features, clever one-liners, or dense chains unless they are clearly simpler than the traditional alternative.

- `2` / `medium` -> prompt from `references/medium-expressiveness.md`
  - Use expressive language features when they make the code clearer, safer, or closer to the domain.
  - Prefer widely understood constructs over advanced ones.
  - Do not use a modern feature only because it is shorter; choose it only when it improves readability.

- `3` / `hot` / `high` -> prompt from `references/high-expressiveness.md`
  - Use the most expressive idioms available in the target language.
  - Prefer modern constructs such as pattern matching, switch expressions, records, data classes, sealed types, pipelines, or named constructors when they improve readability, safety, or closeness to the problem.
  - Keep the code idiomatic and avoid cleverness for its own sake.

## Step 3 - Execute with the selected profile

State the selected temperature in one short line, then perform the requested task using only that profile.

If the choice is invalid, ask again using the same `1/2/3` menu.
