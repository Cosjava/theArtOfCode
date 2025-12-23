package chapter9.listing95;

import chapter9.domain.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Listing 9.5 – One stream constraint with a custom collector
 */
@Slf4j
public class PersonService {
  /**
   * Computes the youngest and oldest person and produces a sorted list of names
   * using a single stream pipeline with {@code Collectors.teeing}.
   *
   * @param persons the list of people to analyze, must not be null
   */
  public void analyzePeople(List<Person> persons) {
    record Stats(Person youngest, Person oldest, List<String> names) {}
    record OldestAndNames(Optional<Person> oldest, List<String> names) {}

    Stats stats = persons.stream().collect(
      Collectors.teeing(
        Collectors.minBy(
          Comparator.comparingInt(Person::age)),
        Collectors.teeing(
          Collectors.maxBy(
            Comparator.comparingInt(Person::age)),
          Collectors.mapping(
            Person::name,
            Collectors.collectingAndThen(
              Collectors.toList(),
              list -> {
                Collections.sort(list);
                return list;
              }
            )
          ), OldestAndNames::new
        ),
        (minOpt, resultOldestAndNames) -> new Stats(
          minOpt.orElse(null),
          resultOldestAndNames.oldest().orElse(null),
          resultOldestAndNames.names()
        )
      )
    );

    log.info("Youngest: {}, oldest: {}, sorted names: {}",
      stats.youngest(),
      stats.oldest(),
      stats.names());
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
