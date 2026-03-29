package chapter10.listing106;

import chapter10.domain.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Listing 10.6 – Final solution
 */
@Slf4j
public class PersonService {
  /**
   * Computes the youngest and oldest person and produces a sorted list of names
   * using a single parallel stream and a composite collector.
   *
   * @param persons the list of people to analyze, must not be null
   */
  public void analyzePeople(List<Person> persons) {
    record Stats(
      List<String> names,
      Optional<Person> youngest,
      Optional<Person> oldest) {}
    record OldestAndYoungest(
      Optional<Person> oldest,
      Optional<Person> youngest) {}

    Stats stats = persons.parallelStream().collect(
      Collectors.teeing(
        getSortCollector(),
        Collectors.teeing(
          Collectors.maxBy(getAgeComparator()),
          Collectors.minBy(getAgeComparator()),
          OldestAndYoungest::new
        ),
        (sortedNames, oldestAndYoungest) -> new Stats(
          sortedNames,
          oldestAndYoungest.youngest(),
          oldestAndYoungest.oldest()
        )
      )
    );
    log.info("Youngest: {}, oldest: {}, sorted names: {}",
      stats.youngest(), stats.oldest(), stats.names());

  }

  /**
   * Creates a collector that extracts names from each {@code Person}, collects
   * them into a list, sorts the list, and returns it.
   *
   * @return a collector that maps persons to sorted names
   */
  private static Collector<Person, ?, List<String>> getSortCollector() {
    return Collectors.mapping(
      Person::name, Collectors.collectingAndThen(
        Collectors.toList(), list -> {
          Collections.sort(list);
          return list;
        }
      )
    );
  }

  /**
   * Provides a comparator for comparing {@code Person} objects by age.
   *
   * @return a comparator that orders persons by ascending age
   */
  private static Comparator<Person> getAgeComparator() {
    return Comparator.comparingInt(Person::age);
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
