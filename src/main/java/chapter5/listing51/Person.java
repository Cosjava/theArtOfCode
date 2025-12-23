package chapter5.listing51;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Listing 5.1 – A simple data transfer object (DTO).
 *
 * <p>This listing demonstrates the most basic form of a DTO, or
 * <em>data transfer object</em>, used to encapsulate and transport
 * structured data between different parts of an application.
 * The {@link Person} class defines a single field, {@code age},
 * along with standard getter and setter methods.</p>
 */
@Slf4j
public class Person implements Serializable {
  private int age;

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    if (age >= 0 && age < 150) {
      this.age = age;
    }
  }

  public static void main(String[] args) {
    Person alice = new Person();
    alice.setAge(20);
    alice.setAge(-2);
    log.info("Age: " + alice.getAge());
  }
}
