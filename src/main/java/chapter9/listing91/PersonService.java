package chapter9.listing91;

import chapter9.domain.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Listing 9.1 – Imperative coding
 */
@Slf4j
public class PersonService {
  /**
   * Computes the youngest and oldest person and produces a sorted list of names
   * in a single pass through the input list.
   *
   * @param persons the list of people to analyze, must not be null
   */
  public void analyzePeople(List<Person> persons) {
    Person youngest = null;
    Person oldest = null;
    Integer minAge = null;
    Integer maxAge = null;
    List<String> names = new ArrayList<>();

    for (Person p : persons) {
      int age = p.age();

      if (minAge == null || age < minAge) {
        minAge = age;
        youngest = p;
      }

      if (maxAge == null || age > maxAge) {
        maxAge = age;
        oldest = p;
      }

      names.add(p.name());
    }
    Collections.sort(names);

    log.info("Youngest: {}, oldest: {}, count: {}, sorted names: {}",
      youngest, oldest, persons.size(), names);


  }

  public static void main(String[] args) {
    PersonService service = new PersonService();

    List<Person> persons = List.of(
      new Person("Alice", 32),
      new Person("Bob", 21),
      new Person("Charlie", 45),
      new Person("Diana", 28)
    );
    service.analyzePeople(persons);
  }
}
