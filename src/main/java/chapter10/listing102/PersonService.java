package chapter10.listing102;

import chapter10.domain.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Listing 10.2 – Functional programming
 */
@Slf4j
public class PersonService {
  /**
   * Computes the youngest and oldest person and produces a sorted list of
   * names.
   * This method highlights the trade-off between functional clarity
   * and strict efficiency. It shows how streams can improve readability
   * when single-pass optimization is not required.
   *
   * @param persons the list of people to analyze, must not be null
   */
  public void analyzePeople(List<Person> persons) {
    Optional<Person> youngest = persons.stream()
      .min(Comparator.comparingInt(Person::age));

    Optional<Person> oldest = persons.stream()
      .max(Comparator.comparingInt(Person::age));

    List<String> sortedNames = persons.stream()
      .map(Person::name).sorted().toList();

    log.info("Youngest: {}, oldest: {}, sorted names {}",
      youngest.orElse(null), oldest.orElse(null), sortedNames);
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
