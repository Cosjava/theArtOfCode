---
name: failure-handling-review
description: 'Review existing code to identify weaknesses in failure handling and propose safer, clearer, and more graceful alternatives without modifying the code directly.'
argument-hint: 'Provide the code to review, along with any relevant context such as the expected behavior, the layer of the system, and any known issues or requirements related to failure handling.'
---

# Failure Handling Review Skill

## Purpose

Use this skill to review existing code and identify weaknesses in failure handling.

The goal is not to rewrite the code directly. The goal is to understand how failures are currently handled, detect error-handling antipatterns, and propose safer, clearer, and more graceful alternatives.

Do not apply code changes unless explicitly asked. Provide recommendations, examples, and reasoning so a human developer can decide what to change.

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

Classify each failure path as one of the following:

- business logic error
- technical issue
- programming error
- fatal error

Use that classification to judge whether the current handling strategy makes sense.

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

## 4. Review the failure handling step by step

Follow this workflow in order.

### Step 1: Identify the failure paths

List the operations that may fail.

For each failure path, identify:

- The operation that may fail.
- The likely exception, error, or invalid result.
- The category of failure.
- The current handling strategy.
- The caller or user affected by the failure.

### Step 2: Check whether the boundary is defended

Review entry points first.

Look for validation, sanitization, authentication, and authorization. If input can cross the boundary unchecked, flag it as an early failure-handling problem.

### Step 3: Review the catch blocks

For each catch block, check:

- Is the caught type specific enough?
- Is the protected block narrow enough?
- Does the catch block handle one meaningful chunk of behavior?
- Does the catch block accidentally absorb programming errors?
- Is the failure path understandable from the code?

Propose more specific catches or narrower protected blocks when useful.

### Step 4: Review the logging strategy

For each logged failure, check:

- Should this failure be logged?
- Is the log level appropriate?
- Is the message unique and useful?
- Does it include enough context for diagnosis?
- Does it avoid sensitive data?
- Is the original exception included?
- Is the same error logged elsewhere?
- Could the log statement itself fail?

Propose better log messages, but do not expose sensitive information in the proposal.

### Step 5: Review exception translation

Check whether technical failures cross architectural boundaries as raw implementation details.

When appropriate, propose:

- Translating low-level exceptions into domain-specific exceptions.
- Returning controlled error responses at API boundaries.
- Preserving the original cause.
- Keeping technical details in logs, not in user-facing messages.

### Step 6: Review fallback and recovery strategy

For technical issues, check whether the code should:

- Retry.
- Fall back to a safe degraded behavior.
- Return cached data.
- Requeue work.
- Stop processing and report a clear error.
- Propagate the failure to a higher layer.
- Fail fast.

Do not invent a fallback that changes business behavior. If the right strategy depends on requirements, state the options and the trade-off.

### Step 7: Review what should not be handled

Identify failures that should probably not be caught locally:

- Programming errors that should be fixed in the code.
- Fatal errors that should not be recovered from inside the corrupted process.
- Failures that need a higher-level policy.
- Failures already handled by a global error handler.

Explain why leaving the failure to another layer may be clearer.

## 5. Do not modify the code

This skill is diagnostic.

Do not rewrite files.  
Do not apply patches.  
Do not silently change exception types, public APIs, return types, or fallback behavior.  
Do not remove logs or catches directly.

Instead, propose changes with enough detail for a developer to review and apply them deliberately.

You may include small illustrative snippets when they clarify the recommendation, but label them as examples.

## 6. Review your own analysis

Before returning the result, review your findings.

Check:

- Did you classify each failure correctly?
- Did you distinguish business errors from technical issues, programming errors, and fatal errors?
- Did you avoid recommending logs for failures that are not actionable?
- Did you avoid exposing sensitive data in proposed log messages?
- Did you preserve public contracts and architectural boundaries?
- Did you avoid proposing fallback behavior that changes business rules?
- Did you clearly separate confirmed problems from uncertain risks?
- Did you remember that the goal is to propose changes, not perform them?

Adjust the recommendations if any proposal introduces new confusion, hides a failure, or changes behavior without justification.

## 7. Expected output

Return a structured review.

Include:

1. **Failure map**  
   A short summary of the main success path and the identified failure paths.

2. **Failure classification**  
   For each failure, classify it as a business logic error, technical issue, programming error, or fatal error.

3. **Findings**  
   For each issue, provide:
    - The location or code area.
    - The signal detected.
    - Why it is a problem.
    - The risk.
    - The proposed improvement.
    - Confidence level: high, medium, or low.

4. **Logging recommendations**  
   Mention which failures should be logged, at what level, and what context should be included or avoided.

5. **Handling strategy recommendations**  
   Propose whether each failure should be validated, translated, logged, retried, propagated, handled with fallback, or allowed to fail fast.

6. **Human-review notes**  
   Highlight decisions that require developer judgment, product requirements, security review, or architecture review.

7. **No automatic changes**  
   End by confirming that no code was modified and that the result is a proposal for human review.