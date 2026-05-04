# Narrative Code Generator

Generate code following the **Narrative Code** principles from *The Art of Code*, Chapter 2.

The user will describe what the code should do. You will produce code structured as a clear story: identifiable plot, correct narrative levels, and well-named useful characters.

---

## Step 1 — Identify the plot

Before writing a single line, determine which plot this code belongs to:

- **Delivering** — pushing data beyond system boundaries (emails, API calls, notifications, file uploads)
- **Cleaning** — removing expired or useless data at runtime (purging old records, clearing temp files)
- **Defense** — guarding integrity (input validation, authentication, authorization, encryption)
- **Archiving** — storing, retrieving, updating, or deleting data (CRUD, document stores)
- **Transformation** — refining raw data into useful form (enrichment, filtering, aggregation, formatting)

If the request mixes plots, keep the top-level story coherent, but separate each plot into clearly named scenes or chapters.

---

## Step 2 — Structure the code at the right narrative level

A **chunk** is a meaningful unit of code that reads as a single recognizable step.
It should be large enough to make sense on its own — such as:
- a function or method call that clearly names one action
- a conditional block representing one decision
- a repetition block representing one repeated operation
- an error-handling block representing one failure-handling step
- a pipeline or chained expression representing one data transformation (as long as it does not exceed 3–4 steps — a longer chain spans multiple chunks)
- a small statement block that together performs one clear task
- a system-boundary interaction (database, HTTP, file) — all mechanical steps to complete it (open, prepare, execute, read result) count as one chunk

A chunk is too small when it has no clear meaning on its own, for example:

- a variable assignment used only as setup
- a single arithmetic operation
- one isolated line inside a larger operation

A chunk is too large when it combines several recognizable steps, for example:

- two unrelated decisions in the same conditional block
- validation and persistence in the same block
- multiple system-boundary interactions grouped together
- a statement block that performs two separate tasks that could each be named
- a branch body (in a conditional, switch statement, or switch expression) that contains more than one expression

**Extract a method only when it earns its place.** A method earns its place when it names a meaningful concept, hides complexity that would clutter the caller, or brings its caller within the chunk budget. If the caller stays within budget and the code reads clearly inline — including simple boolean expressions — keep it there. Indirection for its own sake adds navigation cost without adding understanding.

> **Extraction gate:** Before grouping calls into a new method, count the chunks at the caller level first. Extract when **any** of these conditions holds:
> 1. The caller would **exceed its budget** without the extraction.
> 2. The group names a **cohesive business concept** that the individual call names cannot convey on their own (e.g., `validateEmail` grouping a presence check and a uniqueness check — both validate the email, and the Scene name says so).
> 3. The extraction keeps the caller reading at a **uniform level of abstraction** — when one step names a high-level concept while another names a low-level operation on the same subject, group the low-level operations under the high-level concept so every line at the call site reads at the same altitude.
> 4. The expression is **complex, repeated, or hides intent** that a name would reveal.
>
> Grouping calls that already fit flat should be avoided unless the group names a meaningful business or technical concept.

### Method names

A method name must tell the reader what happens and what it acts on — without opening the method body.

- **Verb + subject**: always combine an action verb with the thing being acted on — `validateBirthDate`, `insertUser`, `checkEmailUniqueness`; never a bare verb (`validate`, `process`, `handle`)
- **Precise scope**: the name must match exactly what the method does — `checkEmailUniqueness` not `checkEmail`; `validateBirthDate` not `validateDate`
- **Readable at the call site**: a reader must understand each step from its name alone; if they must open the method to follow the story, rename it

Build the code across four levels — **Action**, **Scene**, **Chapter**, and **Table of contents** — from lowest-level detail to highest-level orchestration.

### Action
- 1–2 chunks maximum
- May catch an exception only when the handling is complete and local: apply a clear fallback, throw a more appropriate exception, or transform a technical failure into a meaningful domain exception.
- Generic enough to be reused: pass specifics (field names, thresholds) as parameters
- **Does not compose other narrative methods. If it needs to call another Action, promote it to a Scene.**

### Scene
- A Scene should remain focused on one recognizable step of the story.
- Up to 5 chunks.
- Composed of Actions, plus simple supporting logic such as:
  - one or two conditional decisions
  - one repetition block
  - local exception handling
  - simple preparation of values needed by the Actions
- **If a method does not compose Actions and only performs one direct low-level operation, classify it as an Action instead.**

### Chapter
- Orchestrates Scenes and occasional direct Actions toward one business goal.
- Up to 7 chunks
- May include simple supporting logic such as:
  - one or two conditional decisions
  - local exception handling
  - simple preparation of values needed by the Scenes or Actions
- **Must call at least one Scene. If a method calls only Actions, classify it as a Scene instead.**
- The reader should understand the full story without looking inside the scenes or actions
- Total body should stay short: complex logic should be decomposed into smaller Scenes or Actions, not inlined here.

### Table of contents *(only when the story is complex enough to need it)*
- Orchestrates Chapters and occasional direct Scenes or Actions toward a complex use case.
- Up to 7 chunks
- Keep the body short and orchestration-focused.
- If the flow becomes too dense, introduce meaningful Chapters, Scenes, or Actions.
- **Must call at least one Chapter. If a method calls only Scenes, classify it as a Chapter instead.**

---

## Step 3 — Order methods in the class

Methods must appear in the class in this strict top-to-bottom order:

1. **Table of contents** methods (if present)
2. **Chapter** methods
3. **Scene** methods
4. **Action** methods

Within each group, order by call sequence — the first method called appears first. This mirrors the structure of a book: overview before detail, high level before low level. A reader should never have to scroll up to understand a method they just read.

---

## Step 4 — Variable names

Variables are the characters of the story. Apply these rules:

| Variable kind | Naming rule | Example |
|---|---|---|
| Data | Descriptive noun, no abbreviations | `selectedUsersForExclusiveGift` |
| Boolean | Phrased as a yes/no question — `is` or `has` prefix are both valid | `isUserBirthday`, `hasActiveSubscription` |
| Collection | Plural noun | `promotions`, `pendingOrders` |
| Lambda / functional | Verb describing the action | `sendNotification`, `filterExpired` |

Additional rules:
- **Main character** (the central variable or parameter) appears first — first parameter or first line of the method body
- **Secondary characters** are declared as close as possible to where they are used, with the narrowest possible scope
- **No temp variables** that exist only to be returned — return directly (early return)
- **Consistent domain vocabulary** — never mix `user`/`client`/`visitor` for the same concept

---

## Step 5 — Output format

Always end your response with output in this exact structure:

```
Plot: <plot name(s), joined with " + " if mixed>

Code:
<the generated or refactored code>

Narrative structure:
- <Level>: <methodName>
- <Level>: <methodName>
...
```

- **Plot** — one line listing the identified plot(s) from Step 1 (e.g., `Defense + Archiving`, `Transformation`)
- **Code** — the complete generated or refactored code unit, such as a class, module, file, or function depending on the language
- **Narrative structure** — a flat list of every function or method in the generated code unit
---
