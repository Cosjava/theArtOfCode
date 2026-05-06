---
description: Generate code following the **Narrative Code** principles from *The Art of Code*, Chapter 2.
---


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

A **chunk** is a meaningful unit of code that reads as a single recognizable step — such as a method call, a conditional block, a loop, an error-handling block, a pipeline, or a system-boundary interaction (all mechanical steps to complete it count as one chunk).

A chunk is **too small** when it has no meaning on its own (a setup assignment, a single arithmetic operation).

A chunk is **too large** when it combines several recognizable steps:
- two unrelated decisions in the same block
- validation and persistence together
- multiple system-boundary interactions grouped
- a branch body that contains more than one expression

**Extract a method only when it earns its place.** 
A method earns its place when it names a meaningful concept, hides complexity that would clutter the caller, or brings its caller within the chunk budget. 
If the caller stays within budget and the code reads clearly inline — including simple boolean expressions — keep it there. 
Indirection for its own sake adds navigation cost without adding understanding.

> **Extraction gate:** Extract when **any** of these holds:
> 1. The caller would **exceed its budget** without the extraction.
> 2. The group names a **cohesive business concept** the individual names cannot convey alone.
> 3. The extraction keeps the caller at a **uniform level of abstraction**.
> 4. The expression is **complex, repeated, or hides intent** a name would reveal.

### Method names

A method name must tell the reader what happens and what it acts on — without opening the method body.

- **Verb + subject**: always combine an action verb with the thing being acted on — `validateBirthDate`, `insertUser`, `checkEmailUniqueness`; never a bare verb (`validate`, `process`, `handle`)
- **Precise scope**: the name must match exactly what the method does — `checkEmailUniqueness` not `checkEmail`; `validateBirthDate` not `validateDate`
- **Readable at the call site**: a reader must understand each step from its name alone; if they must open the method to follow the story, rename it

Build the code across four levels — **Action**, **Scene**, **Chapter**, and **Table of contents** — from lowest-level detail to highest-level orchestration.

| Level | Budget | Rule                                                                                                                                                     |
|-------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Action** | 1–2 chunks | Does not compose other narrative methods (Actions, Scenes, Chapters). Calling stateless utilities (e.g. `StringUtils`, `DateTimeFormatter`) is allowed.  |
| **Scene** | up to 5 chunks | Composes Actions + simple logic (1–2 conditionals, 1 loop, local error handling). If it composes no Actions, classify as Action instead.                 |
| **Chapter** | up to 7 chunks | Orchestrates Scenes toward one business goal. Must call at least one Scene. The reader should understand the full story without opening Scenes.          |
| **Table of contents** | up to 7 chunks | Orchestrates Chapters. Must call at least one Chapter. Only when the story is complex enough to need it.                                                 |

Methods must appear top-to-bottom: **Table of contents → Chapters → Scenes → Actions**, each group ordered by call sequence. A reader should never have to scroll up to understand a method they just read.

---

## Step 3 — Variable names

| Variable kind | Naming rule | Example |
|---|---|---|
| Data | Descriptive noun, no abbreviations | `selectedUsersForExclusiveGift` |
| Boolean | Phrased as a yes/no question — `is` or `has` prefix are both valid | `isUserBirthday`, `hasActiveSubscription` |
| Collection | Plural noun | `promotions`, `pendingOrders` |
| Lambda / functional | Verb describing the action | `sendNotification`, `filterExpired` |

Use **consistent domain vocabulary** — never mix `user`/`client`/`visitor` for the same concept.

Variables are the characters of the story. Apply these rules:
Additional rules:
- **Main character** (the central variable or parameter) appears first — first parameter or first line of the method body
- **Secondary characters** are declared as close as possible to where they are used, with the narrowest possible scope

---

## Step 4 — Output format

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