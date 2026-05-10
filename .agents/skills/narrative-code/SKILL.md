---
name: narrative-code
description: Refactor or generate code using Narrative Code principles. Use when code has long methods, mixed abstraction levels, unclear orchestration, or vague naming, and you need plot-driven structure (Action, Scene, Chapter, Table of contents) plus a narrative call graph.
---

# Narrative Code Generator

Generate or refactor code following the **Narrative Code** principles from *The Art of Code*, Chapter 2.

The user will describe what the code should do. You will produce code structured as a clear story: identifiable plot, correct narrative levels, and well-named useful characters.

---

## Step 1 - Identify the plot

Before writing a single line, determine which plot this code belongs to:

- **Delivering** - pushing data beyond system boundaries (emails, API calls, notifications, file uploads)
- **Cleaning** - removing expired or useless data (purging old records, clearing temp files)
- **Defense** - guarding integrity (input validation, authentication, authorization, encryption)
- **Archiving** - storing, retrieving, updating, or deleting data (CRUD, document stores)
- **Transformation** - refining raw data into useful form, such as enrichment, filtering, aggregation, formatting, normalization, or deriving new values. If the data is merely cleaned or adapted before storage, treat the main plot as **Archiving**.

---

## Step 2 - Structure the code at the right narrative level

### Chunk definition

A **chunk** is a meaningful unit of code that reads as a single recognizable step - such as a method call, a conditional block, a loop, an error-handling block, a pipeline, or a system-boundary interaction.

A chunk is **too small** when it has no meaning on its own (a setup assignment, a single arithmetic operation).

A chunk is **too large** when it combines several recognizable steps:
- two unrelated decisions in the same block
- validation and persistence together
- multiple system-boundary interactions grouped
- a branch body that contains more than one distinct operation

### Method levels

Build the code across four method levels - **Action**, **Scene**, **Chapter**, and **Table of contents** - from lowest-level detail to highest-level orchestration.

| Level |         Budget | Purpose | Rule                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|---|---------------:|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Action** | Up to 3 chunks | Performs one low-level operation. | Does not compose other narrative methods: Actions, Scenes, or Chapters. If it needs to call another Action in the same file, promote it to a Scene. **Reusability rule:** When identical logic appears in multiple Actions (e.g., "validate required field"), create a single parameterized Action and call it from both places. Pass specifics as parameters: field names, thresholds, error messages, domain concepts. The parameterized Action is still a single narrative step. Example: instead of `validateLastName()` and `validateEmail()`, create `validateRequiredField(value, fieldName, errorMessage)` and call it twice. Calling stateless utilities, framework APIs, external clients, repositories, or Action-level operations from other code units is allowed. **Error handling:** may catch an error only when the handling is simple and local: use a default value, throw a more appropriate exception, or transform a technical failure into a meaningful domain exception. |
| **Scene** | Up to 6 chunks | Focuses on one recognizable step of the story. | Composes Actions plus simple supporting logic: 1-2 conditionals, 1 loop, or error handling. A Scene must call at least one Action. **Downgrade rule:** If a method that would be a Scene calls no Action - only direct inline logic - demote it to an Action. **Error handling:** may catch errors when the failure belongs to this Scene and the handling remains local: apply a fallback, translate the error into a clearer domain exception, or add context before rethrowing. If the error handling coordinates several recovery steps or affects the broader use case, promote it to a Chapter.                                                                                                                                                                                                                                                                                                                                                                                            |
| **Chapter** | Up to 8 chunks | Orchestrates one business goal. | Calls at least one Scene and may include occasional direct Actions. Simple supporting logic is allowed: 1-2 conditionals, 1 loop, or error handling. The reader should understand the full story without opening the Scenes. Keep the body short: complex logic should be decomposed into smaller Scenes or Actions, not inlined here. A Chapter must call at least one Scene. **Downgrade rule:** If a method that would be a Chapter calls no Scene - only Actions or direct inline logic - demote it to a Scene. **Error handling:** may coordinate failures that affect the business goal: recover after a failed Scene, trigger compensating actions, decide whether the flow can continue, or translate lower-level errors into a use-case/domain-level failure.                                                                                                                                                                                                                           |
| **Table of contents** | Up to 8 chunks | Orchestrates a complex use case. | Calls at least one Chapter and may include occasional direct Scenes or Actions. Keep the body short and orchestration-focused, with only light supporting logic or error handling. A Table of contents method must call at least one Chapter. **Downgrade rule:** If a method that would be a Table of contents calls no Chapter - only Scenes, Actions, or direct inline logic - demote it to a Chapter. **Error handling:** may coordinate failures across Chapters, decide whether the complete use case continues or stops, trigger high-level compensation, or translate the final failure into an application-level outcome. Low-level recovery belongs in Actions, Scenes, or Chapters.                                                                                                                                                                                                                                                                                                   |

Methods must appear top-to-bottom: **Table of contents -> Chapters -> Scenes -> Actions**, with each group ordered by call sequence. A reader should never have to scroll up to understand a method they just read.

**Extract a method only when it earns its place.**

Do not extract a method only to satisfy a narrative level. Narrative levels describe useful structure; they are not a quota system. If a caller is already short, readable, and at one clear level of abstraction, keep the story there.

Allow small duplication when it appears only twice and keeps each story easier to read. Do not extract a method just because two places share a few simple lines; extract only when the duplication hides intent, is error-prone, or is likely to change together.

> **Extraction gate:** Extract a method when **any** of these holds:
> 1. The caller would **exceed its chunk budget** without the extraction.
> 2. The group names a **cohesive business concept** the individual names cannot convey alone.
> 3. The extraction keeps the caller at a **uniform level of abstraction**.
> 4. The expression is **complex, repeated, or hides intent** a name would reveal.
>
> **Hard blockers (if any, DO NOT extract):**
> 1. Caller already within chunk budget and readable.
> 2. Extraction does not make the caller easier to read.
> 3. The extracted body contains only one simple chunk.
> 4. The extracted method merely groups already clear calls and adds no domain concept.

When rules conflict, prefer the version that makes the caller easier to understand with the least indirection.

### Method names

A method name must tell the reader what happens and what it acts on - without opening the method body.

- **Verb + subject**: always combine an action verb with the thing being acted on - `validateBirthDate`, `insertUser`, `checkEmailUniqueness`; never a bare verb (`validate`, `process`, `handle`)
- **Precise scope**: the name must match exactly what the method does - `checkEmailUniqueness` not `checkEmail`; `validateBirthDate` not `validateDate`
- **Readable at the call site**: a reader must understand each step from its name alone; if they must open the method to follow the story, rename it

---

## Step 3 - Variable names

| Variable kind | Naming rule | Example |
|---|---|---|
| Data | Descriptive noun, no abbreviations | `selectedUsersForExclusiveGift` |
| Boolean | Phrased as a yes/no question - `is` or `has` prefix are both valid | `isUserBirthday`, `hasActiveSubscription` |
| Collection | Plural noun | `promotions`, `pendingOrders` |
| Lambda / functional | Verb describing the action | `sendNotification`, `filterExpired` |

Use **consistent domain vocabulary** - never mix `user`/`client`/`visitor` for the same concept.

Variables are the characters of the story. Apply these rules:
- **Main character** (the central variable or parameter) appears first - first parameter or first line of the method body
- **Secondary characters** are declared as close as possible to where they are used, with the narrowest possible scope

---

## Step 4 - Review and Mandatory Audit (must pass before output)

Before finalizing, review the generated or refactored code against the narrative rules and run this audit:

- Does every extracted method earn its place?
- Did any method exist only to satisfy a narrative level?
- Are there wrapper methods that merely group already clear calls?
- Is each caller still readable with the fewest useful levels of indirection?
- Do method names reveal exact intent at the call site?
- Are chunks at a consistent level of abstraction?
- Would inlining a method make the story clearer?
- Is duplicated code small enough that extracting it would make the story worse?
- Chunk count per method is within its narrative level budget.
- Each method has an explicit concern tag (`Defense`, `Archiving`, etc.) in the audit notes.
- No forbidden call edge exists (for example `Scene -> Scene`).
- No one-line wrapper exists unless it provides one of:
    - reuse in **3 or more** call sites,
    - encapsulation of non-trivial complexity that is clearer behind a domain-specific name.
- The high-level caller remains within budget after extraction, and still reads clearly if trivial wrappers are inlined.

If a rule is technically satisfied but the code reads worse, revise the code. Prefer the clearest story with the least indirection over strict hierarchy compliance.

If any audit item fails, rewrite and rerun this step before final output.

---

## Step 5 - Output format

Always end your response with output in this exact structure:

```text
Plot: <plot name(s), joined with " + " if mixed>
Description: <short description of the story>

Code:
<the generated or refactored code>

Narrative structure:
<ASCII call graph showing method flow from high-level story to low-level detail>
```

- **Narrative structure** is an ASCII call graph.
- Use indentation and arrows to show hierarchy.
- Start with the highest-level method.
- Include only generated/refactored methods, not external library calls or framework methods.
- Each line must include the narrative level and method name.
- Include the plot in parentheses for Table of contents and Chapter methods.
- Include the plot for Scene methods when it clarifies the story.
- Actions do not need a plot unless useful.

Example:

```text
Narrative structure:
Table of contents: handleUserRegistration (Defense + Archiving + Delivering)
|-- Chapter: createPendingUser (Defense + Archiving)
|   |-- Scene: validateUserRegistration (Defense)
|   |   |-- Action: validateMandatoryField
|   |   |-- Action: validateBirthDate
|   |   `-- Action: validateEmail
|   `-- Scene: savePendingUser (Archiving)
|       `-- Action: insertUser
`-- Chapter: sendConfirmationEmail (Delivering)
    `-- Action: sendEmail
```
