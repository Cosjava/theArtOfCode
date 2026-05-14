---
name: failure-handling
description: Perform a failure handling review of existing code to identify weaknesses in error handling, exception strategy, logging, and boundary protection. Trigger on explicit requests for a failure handling review, error handling audit, or exception handling review. Do NOT trigger on general architecture reviews, code quality reviews, sustainability audits, or refactoring requests — use the architecture-review or sustainability skills for those.
---

# Failure Handling Review Skill

## Purpose

Review code according to the **handling failure** principles from *The Art of Code*, Chapter 7.
Use this skill to review existing code and identify weaknesses in failure handling.
The goal is to understand how failures are currently handled, detect error-handling antipatterns, and propose safer, clearer, and more graceful alternatives.
This skill is diagnostic-only: do not modify code unless explicitly asked. Provide recommendations, examples, and reasoning so a human developer can decide what to change.

## Diagnostic boundary

Do not rewrite files, apply patches, or silently change exception types, public APIs, return types, fallback behavior, logs, or catches.
Instead, propose changes with enough detail for a developer to review and apply deliberately.
You may include small illustrative snippets when they clarify a recommendation, but label them as examples.

## 1. Build a higher-level understanding of the failure story

Before looking for problems, understand what the code is trying to protect.

Ask:

- What is the successful path?
- What can go wrong?
- Where does the input come from?
- Is the failure caused by user behavior, infrastructure, a programming bug, or a fatal runtime condition?
- What layer is this code in: controller, service, domain, persistence, integration, batch, background worker, or UI?
- Who receives the failure: an end user, an API client, another service, an operator, or only the logs?
- Is this code crossing a system boundary such as a file system, database, network call, message queue, external API, or user input?
- Does the code need to recover, retry, fall back, propagate, or fail fast?
- Does the code expose technical details outside the layer where they belong?

List operations that may fail.
Scope the failure map to the most significant paths. List up to five failure paths; if more exist, group minor or similar ones under a single entry with a note such as "three additional validation paths, all business logic errors with identical handling." This keeps the map readable without hiding meaningful risks.
For each failure path, identify:

- The operation that may fail.
- The likely exception, error, or invalid result.
- The category of failure.
- The current handling strategy.
- The caller or user affected by the failure.

Classify each failure path as one of the following:

- **Business logic error**: an expected failure caused by a user action or business rule violation. The system is working correctly, but the request cannot be accepted. Examples: rejected unauthorized access, missing required field, expired discount code, unsupported file format, duplicate email address.

- **Technical issue**: a failure caused by something the application depends on but does not fully control. The code may be correct, but the surrounding infrastructure failed. Examples: database unavailable, network timeout, file system permission denied, external API down, SMTP server unreachable.

- **Programming error**: a defect in the code itself. The program reached a state that should not happen if the code is correct. Examples: `NullPointerException`, invalid assumptions, impossible branch reached, index out of bounds, broken invariant, wrong type cast.

- **Fatal error**: a severe runtime or system-level failure that usually cannot be handled safely inside normal application logic. Examples: out-of-memory error, stack overflow, corrupted process state, JVM crash, container being killed, unrecoverable resource exhaustion.

Use that classification to judge whether the current handling strategy makes sense. A business logic error should usually produce a clear user-facing response. A technical issue should usually be logged and translated or recovered from. A programming error should usually fail fast and be fixed. A fatal error should usually be left to the runtime, container, or supervisor to handle.

Security is cross-cutting rather than a separate failure category. Classify the immediate failure according to its cause, but always flag security impact separately.

## 2. Signals that should trigger the workflow

Flag the code when you see any of these signals.

### Boundary protection problems

- External input is used without validation.
- User input is trusted because it was already checked on the frontend.
- Authorization, authentication, validation, or sanitization is missing near an entry point.
- File paths, URLs, SQL fragments, commands, or uploaded files are built from unchecked input.
- Invalid or unauthorized data can travel deeper into the system before being rejected.

### Lazy catch problems

- Catching `Exception`, `Throwable`, or another overly broad type without a clear reason.
- A bare `except` or generic `catch(e)` hides different failure categories.
- A large `try` block protects too many unrelated operations.
- A catch block makes it unclear what actually failed.
- Programming errors are caught and treated like recoverable failures.
- Future exceptions would be silently absorbed by the same generic catch.

### Logging problems

- Logging only with `System.out`, `System.err`, `printStackTrace`, or equivalent console output.
- Vague log messages such as `"Error occurred"`, `"Exception caught"`, or `"Something went wrong"`.
- The same exception is logged multiple times at different layers.
- Logs contain sensitive information such as passwords, tokens, logins, full file paths, credit card numbers, or personal data.
- The log message lacks context needed for diagnosis.
- The log message includes method calls that could fail and hide the original error.
- The log level does not match the severity of the failure.
- Expected business validation failures are logged as technical errors.
- Technical failures are not logged at all.

### Flawed handling strategy

- Empty catch blocks.
- Returning `null` as a fallback without making the absence explicit.
- Swallowing an exception and continuing as if nothing happened.
- Rethrowing a technical exception across architectural boundaries without translation.
- Wrapping an exception but losing the original cause.
- Throwing a generic exception for a domain-specific failure.
- Using a misleading built-in or framework exception.
- Exposing raw stack traces or technical details to users or API clients.
- Using fallback logic that silently hides the failure of the primary operation.
- Missing retry, fallback, timeout, or circuit-breaker strategy for unstable external calls.
- Retrying without limits or without considering idempotency.
- Treating fatal errors as recoverable inside the same process.

## 3. Decide whether the signal justifies action

A signal does not automatically mean the code is wrong. Before proposing a change, decide whether the current behavior is justified.

Skip or soften the recommendation when:

- The failure is intentionally ignored and the reason is documented.
- The exception is caught broadly at a deliberate top-level boundary.
- A framework already translates or logs the failure consistently.
- The operation is best handled by failing fast.
- Logging would add noise without diagnostic value.
- Changing the exception model would break a public API or contract.
- The code is part of generated code that should not be edited directly.
- The fallback is explicitly required by the business rule and is clearly documented.

When uncertain, say so and explain what information is missing.

## 4. Execution flow

Execution order:
1. Read the code and build the failure map (section 1).
2. Scan for signals (section 2). Note every signal with its location.
3. Filter signals through section 3. Drop or soften any that are justified by context.
4. Review your own analysis (section 5) before writing the report.
5. Produce the report in section 6 format, findings ordered by severity (high → low), then confidence (high → low).
6. End with the no-automatic-changes confirmation.

## 5. Review your own analysis

Before returning the result, review your findings.

Check:

- Did you classify each failure correctly?
- Did you distinguish business errors from technical issues, programming errors, and fatal errors?
- Did you avoid recommending logs for failures that are not actionable?
- Did you avoid exposing sensitive data in proposed log messages?
- Did you preserve public contracts and architectural boundaries?
- Did you avoid proposing fallback behavior that changes business rules?
- Did you clearly separate confirmed problems from uncertain risks?

Adjust the recommendations if any proposal introduces new confusion, hides a failure, or changes behavior without justification.

## 6. Expected output

Return a structured review.

Include:

1. **Failure map**  
   A short summary of the main success path and the identified failure paths, including the failure classification for each path (`business logic error`, `technical issue`, `programming error`, or `fatal error`).

2. **Findings**  
   For each issue, provide, ordered by severity (highest first):
    - Severity (`high`, `medium`, or `low`).
    - The location or code area.
    - The signal detected.
    - The failure classification.
    - Why it is a problem.
    - The risk.
    - The proposed handling recommendation.
    - Confidence level: high, medium, or low.
    - Assumptions/unknowns (required when confidence is medium or low).

3. **Human-review notes**  
   Highlight decisions that require developer judgment, product requirements, security review, or architecture review.

4. **No automatic changes**  
   End by confirming that no code was modified and that the result is a proposal for human review.

## Finding Priority

When multiple findings exist, order them as follows:
1. Severity (high → low)
2. Confidence (high → low)
3. Estimated fix effort (smallest → largest)

Example:

```text
1. Failure map
- Success path: validate input → persist user → send welcome email.
- Failure path 1: invalid email format on input → business logic error.
- Failure path 2: DB timeout during persist → technical issue.
- Failure path 3: SMTP server unreachable → technical issue.
- Failure path 4: NullPointerException on missing config value → programming error.

2. Findings (ordered by severity)
- Severity: high
  - Location: UserService.saveUser
  - Signal: Flawed handling strategy
  - Failure classification: technical issue
  - Why: DB timeout is caught and silently swallowed; caller receives a success response
  - Risk: data loss goes undetected; no alert, no retry, no rollback signal
  - Handling recommendation: catch the timeout explicitly, log with operation context and correlation ID, and rethrow as a typed service exception so the controller can return a 503 and trigger an alert
  - Confidence: high

- Severity: medium
  - Location: NotificationService.sendWelcomeEmail
  - Signal: Logging problem
  - Failure classification: technical issue
  - Why: failure is logged at DEBUG level with only "email failed"
  - Risk: SMTP outages go unnoticed in production; no context for triage
  - Handling recommendation: log at WARN with recipient domain (not full address), SMTP error code, and a retry hint; do not log the full email address
  - Confidence: medium
  - Assumptions/unknowns: assumes no global SMTP failure monitor is already in place

3. Human-review notes
- Confirm whether a global exception handler already enforces one-log-per-failure before adding per-service logging.
- Decide whether a failed welcome email should block user creation or be queued for async retry.

4. No automatic changes
- No code modified; proposal only.
```
