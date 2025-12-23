package chapter5.exercise.solution;

import lombok.extern.slf4j.Slf4j;

/**
 * Solution – Refactoring a mutable class into an immutable record.
 *
 * <p>This solution refactors the classic {@code Employee} class from the
 * exercise
 * into a Java {@code record}.
 * A <em>compact canonical constructor</em> validates that {@code age} and
 * {@code salary} are non-negative, enforcing domain invariants at creation time
 * The additional method {@link #isJunior()} expresses domain logic by returning
 * {@code true} for employees aged 25 or younger.</p>
 */
@Slf4j
record Employee(String name, int age, double salary, String department) {
  public Employee {
    if (age < 0) {
      throw new IllegalArgumentException("Age cannot be negative.");
    }
    if (salary < 0) {
      throw new IllegalArgumentException("Salary cannot be negative.");
    }
  }

  public boolean isJunior() {
    return age <= 25;
  }

  public static void main(String[] args) {
    Employee e1 = new Employee("Alice", 22, 48000, "Engineering");
    Employee e2 = new Employee("Bob", 35, 65000, "Marketing");
    Employee e3 = new Employee("Alice", 22, 48000, "Engineering");

    log.info(e1.toString());
    log.info(e2.toString());
    log.info("{} is junior? {}", e1.name(), e1.isJunior());
    log.info("{} is junior? {}", e2.name(), e2.isJunior());

    log.info("e1 equals e3? {}", e1.equals(e3));
    log.info("e1 equals e2? {}", e1.equals(e2));
    log.info("e1 hashCode: {}", e1.hashCode());
    log.info("e3 hashCode: {}", e3.hashCode());
  }
}

