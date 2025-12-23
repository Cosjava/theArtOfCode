package chapter5.exercise;

import java.util.Objects;

/**
 * Exercise – Refactoring a classic data class into a record.
 *
 * <p>This exercise builds on Chapter 5’s discussion of data carriers and
 * immutability. The provided {@code Employee} class represents a typical
 * mutable
 * JavaBean-style data object with fields for {@code name}, {@code age},
 * {@code salary}, and
 * {@code department}. Your task is to refactor it into a modern {@code record}.
 * Then add a <em>compact canonical constructor</em> that validates that both
 * {@code age} and {@code salary} are non-negative values.
 * Finally Implement a new method {@code isYoungEmployee()} that returns
 * {@code true} for employees aged 25 or younger.
 */
public class Employee {
  private final String name;
  private final int age;
  private final double salary;
  private final String department;

  public Employee(String name, int age, double salary, String department) {
    this.name = name;
    this.age = age;
    this.salary = salary;
    this.department = department;
  }

  public String getName() {return name;}

  public int getAge() {return age;}

  public double getSalary() {return salary;}

  public String getDepartment() {return department;}

  @Override
  public String toString() {
    return "Employee{" +
      "name='" + name + '\'' +
      ", age=" + age +
      ", salary=" + salary +
      ", department='" + department + '\'' +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (!(o instanceof Employee e)) {return false;}
    return age == e.age &&
      Double.compare(e.salary, salary) == 0 &&
      Objects.equals(name, e.name) &&
      Objects.equals(department, e.department);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, salary, department);
  }
}

