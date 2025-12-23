package chapter5.listing56;

import chapter5.domain.BmiCategory;
import chapter5.listing54.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Listing 5.6 – Refactoring for clearer data modeling using records.
 *
 * <p>This listing refactors the rough draft from <strong>listing 5.5</strong>.
 * The goal of this first refactoring is
 * to introduce <em>clear data modeling</em> through the use of records,
 * improving type safety and making the computation flow easier to follow.</p>
 *
 * <p>While the code still performs the same BMI calculations and statistics
 * as before, it now defines small, purpose-driven records to model the data
 * explicitly:</p>
 * <ul>
 *   <li>{@link AgeBmiKey} – a compound key combining age decade and BMI
 *   category
 *       used for grouping operations</li>
 *   <li>{@link Percentage} – a simple record expressing a percentage value with
 *       clear semantic meaning</li>
 *   <li>{@link BmiStat} – represents one line of BMI statistics: age, category,
 *       and percentage of users in that group</li>
 *   <li>{@link BmiGlobalResult} – encapsulates the overall results, including
 *       total users, the age range with the most obese users, and all BMI
 *       stats</li>
 * </ul>
 *
 * <p>This refactoring transforms previously unstructured data into
 * self-descriptive components. Each record clarifies intent and replaces
 * raw collections or generic objects, improving readability and maintainability
 * without altering the algorithm.</p>
 *
 * <p>The next <strong>listing 5.7</strong> will build on this version,
 * introducing a more fluent, top-down structure
 * that makes the computation read like a story.</p>
 */
@Slf4j
public class BmiService {
  record AgeBmiKey(Age age, BmiCategory category) {}

  public record Percentage(double value) {}

  public record BmiStat(Age age, BmiCategory category, Percentage percentage) {}

  public record BmiGlobalResult(long totalUsers, Age ageWithMostObese,
                                List<BmiStat> stats) {}

  public BmiGlobalResult computeBmiStats(List<PhysicalProfile> profiles) {

    long totalUsers = profiles.size();

    Map<AgeBmiKey, Long> countByAgeBmiKey = profiles.stream()
      .collect(Collectors.groupingBy(
        p -> new AgeBmiKey(
          new Age((p.age()
            .value() / 10) * 10), calculateBmiCategory(p)
        ), Collectors.counting()
      ));

    List<BmiStat> stats = countByAgeBmiKey.entrySet().stream()
      .map(entry -> new BmiStat(
        entry.getKey().age(), entry.getKey()
        .category(), new Percentage(100.0 * entry.getValue() / totalUsers)
      )).toList();

    Age ageWithMostObese = countByAgeBmiKey.entrySet().stream()
      .filter(entry -> entry.getKey().category() == BmiCategory.OBESE)
      .max(Comparator.comparingLong(Map.Entry::getValue)).map(Map.Entry::getKey)
      .map(AgeBmiKey::age)
      .orElse(null);

    return new BmiGlobalResult(totalUsers, ageWithMostObese, stats);
  }

  public BmiCategory calculateBmiCategory(PhysicalProfile profile) {
    return switch (profile) {
      case PhysicalProfile(
        _, Age age, WeightInPounds w, HeightInInches h
      ) when age.value() >= 18 -> calculateBmiCategoryForAdult(w, h);
      case PhysicalProfile(
        Gender gender, Age age, WeightInPounds w, HeightInInches h
      ) -> calculateBmiCategoryForChild(gender, age, w, h);
    };
  }

  private BmiCategory calculateBmiCategoryForChild(
    Gender gender, Age age, WeightInPounds w,
    HeightInInches h) {
    // Mock random BMI category for children
    var categories = BmiCategory.values();
    int randomIndex = ThreadLocalRandom.current().nextInt(categories.length);
    return categories[randomIndex];
  }

  public BmiCategory calculateBmiCategoryForAdult(
    WeightInPounds weight, HeightInInches height) {
    double bmi = (weight.value() / (height.value() * height.value())) * 703;
    return switch (bmi) {
      case double v when v < 18.5 -> BmiCategory.UNDERWEIGHT;
      case double v when v < 25.0 -> BmiCategory.NORMAL;
      case double v when v < 30.0 -> BmiCategory.OVERWEIGHT;
      default -> BmiCategory.OBESE;
    };
  }

  public static void main(String[] args) {
    var service = new BmiService();
    var profiles = List.of(
      new PhysicalProfile(Gender.FEMALE, new Age(15), new WeightInPounds(100),
        new HeightInInches(60)),
      new PhysicalProfile(Gender.MALE, new Age(25), new WeightInPounds(176),
        new HeightInInches(70)),
      new PhysicalProfile(Gender.MALE, new Age(35), new WeightInPounds(260),
        new HeightInInches(69)),
      new PhysicalProfile(Gender.FEMALE, new Age(45), new WeightInPounds(150),
        new HeightInInches(65)),
      new PhysicalProfile(Gender.FEMALE, new Age(55), new WeightInPounds(190),
        new HeightInInches(63)),
      new PhysicalProfile(Gender.MALE, new Age(65), new WeightInPounds(240),
        new HeightInInches(70)));
    var results = service.computeBmiStats(profiles);
    log.info("Results {}", results);
  }
}
