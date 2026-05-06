---
name: clarity-of-intent
description: 'Make the data model reveal the purpose of the code.'
argument-hint: 'Describe the code you want to generate or refactor (e.g., "generate a data class for user profiles", "refactor this DTO to follow clarity of intent principles")'
---

# Clarity of Intent Generator

Generate or refactor code following the **Clarity of Intent** principles from *The Art of Code*, Chapter 4.

The data model should reveal the purpose of the code. A reader should understand what a value means, what unit it uses, whether it can change, and which rules make it valid — without opening the class, inspecting the constructor, or searching the codebase.

Use this skill when generating or refactoring data classes, records, DTOs, request/response models, domain values, validation logic, or code with ambiguous primitives or excessive boilerplate.

---

## Table of contents

1. [Model the data first](#step-1--model-the-data-first)
2. [Spot ambiguity](#step-2--spot-ambiguity)
3. [Make domain concepts explicit](#step-3--make-domain-concepts-explicit)
4. [Use small explicit data carriers](#step-4--use-small-explicit-data-carriers)
5. [Flatten structures when they hide meaning](#step-5--flatten-structures-when-they-hide-meaning)
6. [Prefer immutable data when possible](#step-6--prefer-immutable-data-when-possible)
7. [Put invariants close to the data](#step-7--put-invariants-close-to-the-data)
8. [Use explicit categories for closed sets](#step-8--use-explicit-categories-for-closed-sets)
9. [Choose construction that explains itself](#step-9--choose-construction-that-explains-itself)
10. [Place behavior where it clarifies the data](#step-10--place-behavior-where-it-clarifies-the-data)
11. [Naming](#step-11--naming)
12. [Extraction gate: avoid over-modeling](#step-12--extraction-gate-avoid-over-modeling)
13. [Let the clearer data model simplify behavior](#step-13--let-the-clearer-data-model-simplify-behavior)
14. [Refactoring order: smallest blast radius first](#step-14--refactoring-order-smallest-blast-radius-first)
15. [Default behaviour and output format](#step-15--default-behaviour-and-output-format)

> **Public boundaries warning** — when the code is part of a public API contract (OpenAPI-generated DTOs, REST request/response models, published events, persisted JPA entities), renaming a field or wrapping a primitive **breaks consumers or stored data**. In that case, apply the principles to the *internal* domain layer that wraps the contract, and leave the contract itself unchanged. See Steps 3, 6, 7, and 14 for specific guidance.

---

## Step 1 — Model the data first

Before changing types or adding abstractions, identify the data the code is really manipulating.

Ask:

- What is the central data concept?
- Which values represent identity, state, unit, range, or category?
- Which values come from outside the system?
- Which values are temporary, derived, or persisted?
- Which values drive behavior?

Do not start by creating wrappers. First understand the shape and role of the data.

---

## Step 2 — Spot ambiguity

Flag the data model when you see any of these signals:

- primitive types (`int`, `double`, `String`, `boolean`) used for domain concepts
- numeric values without an explicit unit
- several parameters of the same type that can be swapped at the call site
- generic names: `data`, `value`, `type`, `flag`, `status`, `info`, `payload`
- mutable fields where mutation is never needed
- collections exposed without protection
- validation rules scattered far from the data they protect
- nullable fields with no documented meaning
- boilerplate (getters, setters, equals, hashCode) that buries the actual structure

A signal only justifies action if the new type or change earns its place. **Always cross-check Step 2 with [Step 12 — Extraction gate](#step-12--extraction-gate-avoid-over-modeling) before introducing any new type.** Detection (Step 2) and gating (Step 12) are two halves of the same decision.

If a signal forces the reader to guess *and* Step 12 allows extraction, the model needs work.

---

## Step 3 — Make domain concepts explicit

Replace a primitive with a domain type **only when the type can carry a rule, a unit, an identity, or a state the primitive cannot enforce on its own**. Naming alone is not a reason to wrap.

| Primitive | Useful domain type | Triggers extraction when… |
|---|---|---|
| `String email` | `EmailAddress` | format must be validated |
| `int age` | `Age` | a valid range exists |
| `double amount` | `Money` | currency or arithmetic rules apply |
| `int height` | `HeightInCentimeters` | unit confusion is possible |
| `String country` | `CountryCode` | a closed set / format applies |
| `String id` | `CustomerId`, `OrderId` | swapping different IDs is plausible |
| `String type` | enum or sealed type | values come from a closed set |
| `boolean active` | `AccountStatus` | more than two states exist or are coming |

```java
// before
User user = new User("John", 180, 70);

// after
PhysicalProfile profile = new PhysicalProfile(
  "John",
  new WeightInPounds(180),
  new HeightInInches(70)
);
```

If none of the triggers above apply, keep the primitive.

> **Public-contract caveat** — never wrap a primitive directly inside an OpenAPI-generated DTO, a published event payload, or a persisted JPA column: it would change the wire format or the database schema. Instead, wrap the primitive in the **internal domain object** built from the DTO (see Step 7).

---

## Step 4 — Use small explicit data carriers

When several values travel together and represent one concept, group them into a small explicit data carrier.

Use this for:

- composite keys
- ranges
- units
- coordinates
- short-lived method results
- intermediate transformation results

Prefer:

```java
record DateRange(LocalDate startDate, LocalDate endDate) {}
record CustomerOrderKey(CustomerId customerId, OrderId orderId) {}
record ValidationResult(boolean isValid, List<String> errors) {}
```

over unrelated parameters, generic maps, arrays, or tuples.

A small data carrier earns its place when it gives a meaningful name to values that already belong together.

> **Step 4 vs Step 5** — Step 4 *introduces* a carrier when several loose values belong together (no existing structure, or a flat parameter list). Step 5 *replaces* an existing nested structure (`Map<A, Map<B, C>>`, deep DTO chains) when its shape hides intent. If you are creating a new aggregate, you are in Step 4. If you are reshaping one that already exists, you are in Step 5.

---

## Step 5 — Flatten structures when they hide meaning

Deeply nested structures can obscure intent. When a nested structure forces the reader to mentally reconstruct the data shape, prefer a flatter explicit model.

Watch for:

- nested maps
- maps with string keys representing domain concepts
- deeply nested DTOs used only to reach one value
- repeated access chains such as `a.b().c().d()`

Prefer composite keys or explicit records when they make the structure clearer.

```java
// harder to reason about
Map<CustomerId, Map<ProductId, Discount>> discounts;

// clearer
Map<CustomerProductKey, Discount> discounts;

record CustomerProductKey(CustomerId customerId, ProductId productId) {}
```

Flatten only when it improves understanding. Do not flatten a structure that naturally represents a meaningful hierarchy.

---

## Step 6 — Prefer immutable data when possible

Use immutable structures (records, value objects, `final` fields) when the data is never modified after creation. When exposing collections, return defensive copies or unmodifiable views. Replace setters with transformation methods that return new values.

**Framework-mandated mutability is allowed**: JPA entities, Jackson DTOs without `@JsonCreator`, OpenAPI-generated models, MapStruct sources. Keep them mutable, but do not extend that mutability to the domain layer that wraps them.

---

## Step 7 — Put invariants close to the data

Validation rules belong on the type they protect. Use constructors, compact constructors, factory methods, or validation functions to reject invalid state at creation time.

```java
record Age(int value) {
  public Age {
    if (value < 0 || value > 130) {
      throw new IllegalArgumentException("Age must be between 0 and 130.");
    }
  }
}
```

Make invalid combinations impossible when the language allows it: enums, sealed hierarchies, dedicated result types.

**When the type is generated** — OpenAPI client models, JPA entities, protobuf — you cannot add invariants to it. Instead, enforce them in:

- a wrapping value object built from the generated DTO, or
- the service / factory that constructs the DTO.

Keep low-level technical validation (`@NotNull`, `@Size`) separate from domain invariants.

---

## Step 8 — Use explicit categories for closed sets

When a value can only belong to a fixed set of known states or categories, represent that constraint explicitly.

Prefer:

- enums
- sealed types
- discriminated unions
- tagged union types
- pattern-matchable variants

Avoid:

- raw strings
- numeric codes
- booleans that are starting to represent several states

```java
enum PaymentStatus {
  PENDING,
  PAID,
  FAILED,
  REFUNDED
}
```

Use explicit categories when the set of values is closed and meaningful to the domain.

---

## Step 9 — Choose construction that explains itself

The way an object is built should reveal intent at the call site.

- **constructor** — few clear parameters
- **named parameters** — when the language supports them
- **builder** — many optional parameters, such as Lombok `@Builder` in Java
- **factory method** — when construction has a meaningful domain name, such as `Subscription.trialFor(user)` or `Invoice.paid(order, payment)`
- **value object** — when same-typed parameters can be swapped

```java
// avoid — positional arguments, ambiguous nulls, swappable same-typed values
Person p = new Person("John", "Doe", null, null, "France", 38000, 25);

// prefer — each value is named and typed at the call site
PersonProfile profile = PersonProfile.builder()
  .firstName("John")
  .lastName("Doe")
  .country(new CountryCode("FR"))
  .salary(new Money(38000, Currency.EUR))
  .age(new Age(25))
  .build();
```

---

## Step 10 — Place behavior where it clarifies the data

Apply this heuristic:

- the method **only reads its own fields** → put it on the type
- the method needs another **collaborator** — repository, client, service, mapper → put it in a service
- the method **formats or derives a value** intrinsic to the concept → put it on the type

```java
// good — only reads own fields
record Employee(String name, int age, Money salary) {
  boolean isJunior() {
    return age <= 25;
  }
}

// bad — needs a collaborator, belongs in a service
record Employee(...) {
  void generatePayrollReportAndSendEmail() {
    // uses repository + mailer
  }
}
```

When behavior depends on a closed set of data categories, prefer an explicit branching structure such as a switch expression, pattern matching, or equivalent language construct.

Use explicit branching when:

- the set of cases is closed
- each branch is short
- the behavior is directly driven by the data category
- the compiler or type system can help check exhaustiveness

Use polymorphism or Strategy when:

- new behavior must be added without changing existing branching code
- each behavior is complex
- behavior is injected or configured dynamically

---

## Step 11 — Naming

- **Types**: precise domain nouns — `EmailAddress`, `Money`, `CustomerId`, `PaymentStatus`. Avoid vague names like `Data`, `Info`, `Payload`, `Wrapper`, `Helper`. Project-defined technical suffixes that convey a layer (`Dto`, `Vm`, `Resource`, `Mapper`) remain allowed when they match the codebase convention.
- **Fields**: meaning and unit — `weightInPounds`, `amountIncludingTax`, `expiresAt`. Avoid `value`, `type`, `date`, `flag`.
- **Booleans**: yes/no questions — `isActive`, `hasExpired`, `canReceivePromotion`. Replace with an enum once more than two states appear.

---

## Step 12 — Extraction gate: avoid over-modeling

A new type must earn its place. **Skip the wrapper when any of these holds**:

- the existing name is already clear
- the value has no rule, unit, ambiguity, or risk of confusion
- the type would be used only once and adds no protection
- the abstraction is more obscure than the primitive it replaces
- it only moves complexity elsewhere
- the surrounding code is generated and the wrapper would not be reachable from the call site that matters

> A model is good when it reduces confusion. It is excessive when it adds ceremony without reducing risk.

---

## Step 13 — Let the clearer data model simplify behavior

After improving the data model, re-check the surrounding logic.

Look for behavior that can now become simpler because the data is more explicit:

- remove repeated interpretation of raw values
- remove defensive checks made unnecessary by invariants
- replace string comparisons with enums or sealed-type branches
- replace argument-order comments with explicit types
- simplify conditions that now have named domain concepts

A clearer model should reduce the amount of interpretation needed in the behavior.

---

## Step 14 — Refactoring order: smallest blast radius first

Work outward from the cheapest, safest change:

1. **Rename** — variables, fields, parameters. Cheap inside a module; **not free across public boundaries** (OpenAPI fields, JSON contracts, persisted columns, published event keys). On a public boundary, keep the external name and rename only the internal mapping target.
2. **Wrap** — introduce domain types where Step 3 triggers apply and Step 12 allows.
3. **Group** — introduce small data carriers where values already belong together.
4. **Flatten** — replace nested structures when they hide meaning.
5. **Freeze** — make data immutable where mutation is unnecessary.
6. **Guard** — move invariants onto the type, or its factory if the type is generated.
7. **Categorize** — replace raw strings, numeric codes, or overloaded booleans with explicit categories.
8. **Reshape construction** — replace long argument lists with builders or factory methods.
9. **Simplify behavior** — let the clearer model remove interpretation logic.
10. **Re-check Step 12** — drop any type added earlier that does not earn its place.

Each step must leave the code compiling and tests green. Do not refactor everything at once.

---

## Step 15 — Default behaviour and output format

**Without an explicit instruction, refactor the code** — do not just identify issues. Analysis-only responses are acceptable only when the user explicitly asks for a review.

**Stop condition:** if Step 2 finds zero signal, return the code unchanged and explain why no change earns its place.

Always end the response with:

```markdown
Signals found:
- <signal from Step 2>
- <signal from Step 2>

Refactored code:
<code>

Data modeling choices:
- <choice 1>
- <choice 2>

Behavior simplifications (from Step 13):
- <what interpretation logic was removed or replaced>

Trade-offs:
<what became more explicit, and what complexity was added if any, including any public-boundary concession>
```

If a section has nothing to report, keep the heading and write `- none`. This keeps responses comparable across refactors.

---
