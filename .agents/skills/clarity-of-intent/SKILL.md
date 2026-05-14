---
name: clarity-of-intent
description: "Refactor or generate code so data models reveal intent through explicit domain types, precise names, small carriers, invariants, and immutability. Trigger on explicit requests for a clarity of intent refactor, data model refactor, or domain type review. Do NOT trigger on general architecture reviews, failure handling reviews, sustainability audits, or code quality reviews — use the architecture-review, failure-handling, or sustainability skills for those."
---

# Clarity of Intent Generator

Generate or refactor code following the **Clarity of Intent** principles from *The Art of Code*, Chapter 4.

The data model should reveal the purpose of the code. A reader should understand what a value means, what unit it uses, whether it can change, and which rules make it valid — without opening its definition, inspecting its construction logic, or searching the codebase.

Use this skill when generating or refactoring data structures, data carriers, request/response models, domain values, validation logic, or code with ambiguous primitive values, unclear construction, unnecessary mutability, or excessive boilerplate.

---


> **Public boundaries warning** — when the code is part of a public API contract (OpenAPI-generated DTOs, REST request/response models, published events, persisted JPA entities), renaming a field or wrapping a primitive **breaks consumers or stored data**. In that case, apply the principles to the *internal* domain layer that wraps the contract, and leave the contract itself unchanged.

---

## Step 1 — Model the data first

Before making changes, identify the data the code is really manipulating.

Ask:

- What is the central data concept?
- Which values represent identity, state, unit, range, or category?
- Which values come from outside the system, and which are internal?
- Which values are temporary, derived, or persisted?
- Which values drive behavior?
- Which values naturally belong together?
- Which values are part of a public contract or generated model that cannot be changed directly?

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

A signal only justifies action if the change earns its place. Skip the change when:

- the existing name is already clear
- the value has no rule, unit, ambiguity, or risk of confusion
- the type would be used only once and adds no protection
- the abstraction is more obscure than the primitive it replaces
- it only moves complexity elsewhere

---

## Step 3 — Use names that reveal the role of the data

Before wrapping any type, ensure names carry meaning:

- **Types / carriers / categories**: precise domain nouns, such as `EmailAddress`, `Money`, `CustomerId`, `PaymentStatus`.
- **Fields / properties / parameters**: names that reveal meaning and unit, such as `weightInPounds`, `amountIncludingTax`, `expiresAt`.
- **Booleans**: yes/no questions, such as `isActive`, `hasExpired`, `canReceivePromotion`.

Avoid vague names such as `Data`, `Info`, `Payload`, `Wrapper`, `Helper`, `value`, `type`, `date`, or `flag`, unless they are established project conventions.

Renaming alone is often the smallest and safest change. Apply it before reaching for a new type.

---

## Step 4  — Make domain concepts explicit

Replace any raw type — primitive or object — with a domain type **only when the new type can carry a rule, a unit, an identity, or a state the raw type cannot enforce on its own**. Naming alone is not a reason to wrap.

| Raw type | Useful domain type | Triggers extraction when… |
|---|---|---|
| `String email` | `EmailAddress` | format must be validated |
| `int age` | `Age` | a valid range exists |
| `double amount` | `Money` | currency or arithmetic rules apply |
| `int height` | `HeightInCentimeters` | unit confusion is possible |
| `String country` | `CountryCode` | a closed set / format applies |
| `String id` | `CustomerId`, `OrderId` | swapping different IDs is plausible |
| `String type` | enum or sealed type | values come from a closed set |
| `boolean active` | `AccountStatus` | more than two states exist |
| `LocalDate date` | `SubscriptionStartDate`, `InvoiceDueDate` | timezone assumptions exist, or the role of the date is ambiguous |
| `LocalDateTime timestamp` | `CreatedAt`, `ExpiresAt` | multiple timestamps exist and their roles can be confused |

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

> **Public-boundary reminder** — if this value belongs to a public contract or persisted model, apply the wrapper in the internal domain layer instead.

---

## Step 5 — Use small explicit data carriers

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

> **Step 5 vs Step 6** — Use Step 5 when loose values already belong together and need a named carrier. Use Step 6 when an existing nested structure hides intent and should be reshaped.

---

## Step 6 — Flatten structures when they hide meaning

Deeply nested structures can obscure intent. When a nested structure forces the reader to mentally reconstruct the data shape, prefer a flatter explicit model.

Flatten only when the flatter representation with explicit names (composite keys or carrier records) is easier to understand at call sites and in iteration or aggregation logic.

Common flattening candidates:

- nested structures: structures that contain other structures inside them, such as maps of maps, dictionaries of dictionaries, maps of lists, lists of maps, nested objects, or nested arrays
  - `Map<K1, Map<K2, V>>` → `Map<CompositeKey(K1, K2), V>` with an explicit composite key record
  - `Map<K, List<V>>` → if V values group conceptually, consider `Map<CompositeKey, V>` or a collection carrier
  - Lists of maps: `List<Map<K, V>>` → consider a record grouping the map's entries
- maps, dictionaries, or key/value structures where string keys represent domain concepts
- deeply nested DTOs, records, or objects used only to reach one value
- repeated access chains such as `a.b().c().d()`
- tuples, arrays, or lists where position carries meaning (replace with named records)

**When flattening, always create an explicit composite key or carrier record** to name the grouped dimensions. Never leave a flat map with raw primitive keys if the original was nested.

```java
// harder to reason about — nested structure, candidate for flattening
Map<CustomerId, Map<ProductId, Discount>> discounts;

// clearer — one flat map with explicit composite key
Map<CustomerProductKey, Discount> discounts;

record CustomerProductKey(CustomerId customerId, ProductId productId) {}
```

Do not flatten a structure that naturally represents a meaningful hierarchy and is used as a hierarchy (e.g., a tree where you regularly access intermediate levels). But when you are iterating all leaf values or aggregating across all keys, flattening is usually the right choice.

---

## Step 7 — Prefer immutable data when possible

Data that does not change after creation should be impossible to change. Immutability removes an entire class of bugs where a value is modified somewhere unexpected.

**Make the type immutable at construction**
- Use `record` (Java), `data class` (Kotlin), or `@Value` (Lombok) for data carriers
- Mark fields `final` when records are not available
- Reject invalid state in the constructor so the object is always valid from the moment it exists

**Expose collections safely**
- Never return a mutable internal collection directly — return a defensive copy or an unmodifiable view
- Never accept a mutable collection parameter and store it directly — copy it at construction time

```java
// unsafe — caller can mutate the internal list
public List<LineItem> getItems() { return items; }

// safe — caller gets a read-only view
public List<LineItem> getItems() { return Collections.unmodifiableList(items); }
```

**When mutability is unavoidable**
Some frameworks, serializers, ORMs, or schema-generated models require mutable structures. Accept that mutability at the boundary and keep it there. The internal domain layer should still prefer immutable models.

---

## Step 8 — Put invariants close to the data

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

If the type is generated or framework-owned, enforce invariants in the internal wrapper, factory, or mapping layer instead.

---

## Step 9 — Use explicit categories for closed sets

When a value can only belong to a fixed set of known states or categories, represent that constraint explicitly instead of relying on free-form values.

Prefer the equivalent construct in the target language, such as:

- enums
- union types
- sealed or closed hierarchies
- tagged/discriminated variants
- constant sets with validation when the language has no stronger option

Avoid:

- raw strings
- numeric codes
- loosely validated constants
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

## Step 10 — Choose construction that explains itself

The way an object is built should reveal intent at the call site.

- **simple constructor or initializer** — when there are only a few clear parameters
- **named parameters / keyword arguments** — when the language supports passing arguments by name
- **builder** — when there are many optional parameters or many values of the same basic type
- **factory method** — when the type can be created in multiple meaningfully different ways, when construction represents a domain event, or when the name communicates a precondition the constructor cannot express. Examples: `Subscription.trial(user)` vs `Subscription.paid(user, plan)`, `Invoice.paid(order, payment)`
- **explicit value types** — when same-typed values can be swapped or need units, rules, or identity

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

## Step 11 — Move behavior onto the type

After clarifying the data model, look for logic in the surrounding code that belongs on the type itself.

Move logic onto the type when:

- a condition repeatedly inspects the internals of a type to answer a domain question — extract it as a method: `expiresAt != null && expiresAt.isBefore(now)` → `subscription.hasExpired()`
- a calculation is always performed on a type's values — move it onto the type: `amount * quantity` repeated at call sites → `lineItem.totalAmount()`
- a formatting or conversion is repeated at multiple call sites — move it: `"%s %s".formatted(first, last)` → `name.fullName()`
- a validation is re-checked after construction — if the type already enforces it, delete the check; if it doesn't, move it to the constructor

Do not move behavior onto the type when:
- it requires dependencies the type should not know about (repositories, services, external APIs)
- it belongs to a public contract or generated model that cannot be changed
- it is application logic that orchestrates multiple types rather than describing one concept

---

## Step 12 — Let the clearer data model simplify behavior

**Use idiomatic language features unlocked by the cleaner model**

A clearer model often makes modern language constructs applicable where they weren't before. Look for opportunities to use:

- **Pattern matching / sealed type branches** — when a raw String or int is replaced by a sealed hierarchy or enum, `switch` expressions with exhaustive case coverage become possible and safer
- **Stream pipelines** — when a `List<Map<String, Object>>` becomes a `List<Order>`, filtering, mapping, and grouping become readable: `orders.stream().filter(Order::isPending).collect(...)`
- **Optional or result types** — when a nullable raw value becomes an `Optional<CustomerId>` or a typed result, `map`, `flatMap`, and `orElse` replace nested null checks
- **Destructuring / record deconstruction** — when a tuple or positional array becomes a named record, pattern matching can destructure it directly in a `switch` or `instanceof` check
- **Collectors and groupingBy** — when flat maps with composite keys replace nested maps, `Collectors.groupingBy` with a key extractor becomes natural and readable

Apply these only when they genuinely simplify the call site. Do not introduce streams or pattern matching where a simple loop or condition is already clear.

Re-check the surrounding logic for interpretation code that is now redundant.

Look for and remove:

- **Defensive checks made unnecessary by invariants** — `if (age < 0)` guards scattered through the code that the `Age` constructor now rejects at creation time; delete them
- **Null checks made unnecessary by construction** — `if (customerId == null)` checks that disappear when `CustomerId` is always constructed non-null and passed as a required parameter
- **Range or format checks made unnecessary by the type** — `if (!email.contains("@"))` or `if (amount < 0)` conditions that the domain type already enforces in its constructor or factory method
- **Argument-order comments made unnecessary by types** — inline comments like `// width, then height` or `// start, then end` that existed only because two parameters shared the same raw type; remove once each has a distinct type

A clearer model should reduce the amount of interpretation needed in the behavior. If no simplification is possible, note that explicitly.

---

## Refactoring order: smallest blast radius first

When refactoring existing code, apply changes from the smallest blast radius to the largest:

1. Rename unclear data.
2. Group values that already belong together.
3. Wrap ambiguous primitives only when the new type earns its place.
4. Replace raw categories with explicit ones.
5. Put invariants close to the data.
6. Make data immutable when mutation is unnecessary.
7. Flatten nested structures only when they hide meaning.
8. Reshape unclear construction.
9. Simplify behavior around the clearer data model.
10. Remove any type, carrier, or method introduced during refactoring that adds indirection without making the code easier to understand at its call sites.

Each step must preserve behavior and keep the code compiling.

## Before writing the output — audit checklist

Before producing the final response, verify:

- [ ] All primitive values with rules, units, identity, or ambiguity wrapped in domain types?
- [ ] All vague names replaced with precise domain concepts?
- [ ] All related values grouped into explicit carriers?
- [ ] All positional tuples or arrays replaced with named records?
- [ ] All nested DTOs/objects that hide intent reshaped?
- [ ] If there are exceptions, are they explicitly justified in comments?
- [ ] Logic that belongs on a type moved off the call site (Step 11)?
- [ ] Idiomatic language features applied where the cleaner model unlocks them (Step 12)?
- [ ] No public contract or persisted model silently modified — or concession explicitly noted in Trade-offs?

---

## Default behaviour and output format

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

Behavior simplifications:
- <what interpretation logic was removed or replaced>

Trade-offs:
<what became more explicit, and what complexity was added if any, including any public-boundary concession>
```

If a section has nothing to report, keep the heading and write `- none`. This keeps responses comparable across refactors.

---

## Example

**Input:**
```java
public class Order {
    public String status;
    public double amount;
    public String customerId;
    public String date;
    public boolean flag;
}
```

**Output:**

```
Signals found:
- `status` is a raw String representing a closed set of states
- `amount` is a raw double with no currency or unit
- `customerId` is a raw String that could be swapped with other IDs
- `date` is a raw String with no role or format enforced
- `flag` is a boolean with no meaningful name

Refactored code:
record Order(
  OrderStatus status,
  Money amount,
  CustomerId customerId,
  OrderPlacedAt placedAt,
  boolean isEligibleForDiscount
) {}

enum OrderStatus { PENDING, CONFIRMED, SHIPPED, CANCELLED }
record Money(BigDecimal amount, Currency currency) {}
record CustomerId(UUID value) {}
record OrderPlacedAt(LocalDateTime value) {}

Data modeling choices:
- OrderStatus replaces raw String; closes the set of valid states at the type level
- Money wraps amount and currency together; prevents unit-free arithmetic
- CustomerId wraps UUID; prevents swapping with other ID types at call sites
- OrderPlacedAt names the role of the timestamp; removes ambiguity between creation, placement, and shipping dates
- isEligibleForDiscount replaces `flag` with a yes/no question name

Behavior simplifications:
- switch branches on status can now use enum cases instead of string literals
- currency checks downstream are removed; Money carries the currency
- isEligibleForDiscount moved onto Order as a method; call sites no longer reconstruct the condition

Trade-offs:
- More types to maintain; justified because each type carries a rule or prevents a swap
- No public-boundary concessions needed; this is an internal domain model
```