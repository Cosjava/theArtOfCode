package chapter4.listing41;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Listing 4.1 – A richer data transfer object with builder and equality methods.
 *
 * <p>This listing models a person’s identity with two attributes:
 * {@code firstname} and {@code lastname}. It demonstrates common techniques
 * used in classic Java object design, including:</p>
 *
 * <ul>
 *   <li>Encapsulation through private fields and getter/setter methods</li>
 *   <li>Custom implementations of {@link #equals(Object)} and {@link #hashCode()}
 *       for proper value-based equality</li>
 *   <li>Readable output via {@link #toString()}</li>
 *   <li>The <em>Builder pattern</em> for flexible and fluent object construction</li>
 * </ul>
 */
@Slf4j
public class Person {
  private String firstname;
  private String lastname;


  public Person(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  @Override
  public String toString() {
    return "Person{" + "firstname='" + firstname + '\'' + ", lastname='" + lastname + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person person)) return false;
    return Objects.equals(firstname, person.firstname) && Objects.equals(lastname, person.lastname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastname);
  }

  private Person(Builder builder) {
    this.firstname = builder.firstname;
    this.lastname = builder.lastname;
  }

  public static class Builder {
    private String firstname;
    private String lastname;

    public Builder setFirstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder setLastname(String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Person build() {
      return new Person(this);
    }
  }

  public static void main(String[] args) {

    Person alice = new Person("Alice", "Smith");
    log.info("Created with constructor: " + alice);

    Person bob = new Person.Builder()
      .setFirstname("Bob")
      .setLastname("Johnson")
      .build();
    log.info("Created with builder: " + bob);

    Person anotherBob = new Person.Builder()
      .setFirstname("Bob")
      .setLastname("Johnson")
      .build();

    log.info("\nEquality check:");
    log.info("bob.equals(anotherBob) = " + bob.equals(anotherBob));
    log.info("bob.hashCode() == anotherBob.hashCode() = " +
      (bob.hashCode() == anotherBob.hashCode()));
  }
}

