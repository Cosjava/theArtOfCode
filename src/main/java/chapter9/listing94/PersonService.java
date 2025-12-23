package chapter9.listing94;

import chapter9.domain.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Listing 9.4 – A smaller memory footprint in imperative coding
 */
@Slf4j
public class PersonService {
  /**
   * Computes the youngest and oldest person and produces a sorted list of names
   * in a single pass through the list.
   *
   * @param persons the list of people to analyze, must not be null
   */
  public void analyzePeople(List<Person> persons) {
    String youngestName = null;
    String oldestName = null;
    int minAge = Integer.MAX_VALUE;
    int maxAge = Integer.MIN_VALUE;
    List<String> names = new ArrayList<>(persons.size());

    for (Person p : persons) {
      if (p.age() < minAge) {
        minAge = p.age();
        youngestName = p.name();
      }
      if (p.age() > maxAge) {
        maxAge = p.age();
        oldestName = p.name();
      }
      names.add(p.name());
    }

    Collections.sort(names);

    log.info("Youngest: {}, oldest: {}, count: {}, sorted names: {}",
      youngestName, oldestName, persons.size(), names);

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
