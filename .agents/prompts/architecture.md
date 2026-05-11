Review the following code, module, or package for low cohesion and high coupling.

This is a review-only task. Do not rewrite the code unless explicitly asked.

Before reviewing, build a shared understanding of the architecture.

Interview me about every aspect of the design that is necessary to evaluate cohesion and coupling correctly. Walk down each branch of the design tree, resolving dependencies between decisions one by one.

Ask the questions one at a time. Do not ask several questions in the same message.

For each question:
- explain why the answer matters for evaluating cohesion or coupling;
- provide your recommended answer based on the code and context available;
- ask me to confirm or correct it.

If a question can be answered by exploring the codebase, inspect the codebase instead of asking.

Focus your questions on:
- the responsibility of the module or package;
- the architectural layers and allowed dependencies;
- which dependencies are stable and which are likely to change;
- whether public models are internal or part of an external contract;
- expected future changes;
- known pain points;
- boundaries between business logic, infrastructure, and presentation;
- whether current abstractions solve a real change problem or only add ceremony.

Once the architecture is clear, review for signs of low cohesion:
- classes or modules mixing unrelated responsibilities;
- methods in the same class that belong to different business concepts;
- utility or manager classes that collect unrelated behavior;
- fields used by only a small subset of methods;
- methods that change for different reasons;
- code that would be hard to name clearly because it does too many things.

Then review for signs of high coupling:
- many dependencies between classes, modules, or layers;
- business logic depending directly on technical details such as databases, HTTP clients, frameworks, or file systems;
- changes in one class likely requiring changes in many others;
- public APIs exposing internal models or database entities;
- circular dependencies;
- duplicated knowledge shared across multiple components;
- constructors or methods with many collaborators.

For each finding, include:
- Type: Low cohesion or High coupling
- Location
- Issue
- Architectural assumption used
- Why it harms durability
- Smallest safe improvement
- Trade-off with simplicity or readability
- Severity: high, medium, or low
- Confidence: high, medium, or low
- Assumptions, if confidence is medium or low

Do not recommend adding an abstraction unless it solves a concrete dependency or change problem.
Do not apply SOLID principles mechanically.
Prefer simple refactorings that make responsibilities clearer and reduce ripple effects.