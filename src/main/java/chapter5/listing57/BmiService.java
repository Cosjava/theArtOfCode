package chapter5.listing57;

import chapter5.domain.BmiCategory;
import chapter5.listing54.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Listing 5.7 – Refactoring for narrative clarity.
 *
 * <p>This listing refactors the <strong>listing 5.6</strong> into a more
 * readable and expressive design. The focus here is not just on correctness or
 * data modeling, but on <em>narrative code</em>—code that communicates its intent
 * clearly.</p>
 *
 * <p>In this version, the {@link BmiService} logic is decomposed into small,
 * purpose-specific methods such as:</p>
 * <ul>
 *   <li>{@link #calculateCountByAgeAndBmiKey(List)} – counts how many users fall into
 *       each age decade and BMI category;</li>
 *   <li>{@link #calculatePercentageByBmiCategory(Map, long)} – transforms counts
 *       into meaningful percentages;</li>
 *   <li>{@link #findAgeWithMostObese(Map)} – identifies the age group with the
 *       highest number of obese users.</li>
 * </ul>
 *
 * <p>This modular approach allows the {@link #computeBmiStats(List)} method
 * to read like a narrative:
 * <em>count, calculate percentages, identify the most affected age group, return results.</em>
 * The algorithm remains identical to <strong>listing 5.6</strong>, but the refactoring improves
 * comprehension by giving each step a clear and expressive name.</p>
 *
 *
 * <p>This listing concludes the BMI computation example series:
 * <ul>
 *   <li>Listing 5.5 introduced a rough, unclear first draft;</li>
 *   <li>Listing 5.6 added structure through explicit data modeling with records;</li>
 *   <li>Listing 5.7 (this version) achieves true clarity through narrative refactoring.</li>
 * </ul>
 * </p>
 */
@Slf4j
public class BmiService {
  record AgeBmiKey(Age age, BmiCategory category) {}

  public record Percentage(double value) {}

  public record BmiStat(Age age, BmiCategory category, Percentage percentage) {}

  public record BmiGlobalResult(long totalUsers, Age ageWithMostObese, List<BmiStat> stats) {}

  public BmiGlobalResult computeBmiStats(List<PhysicalProfile> profiles) {
    long totalUsers = profiles.size();
    Map<AgeBmiKey, Long> countByAgeBmiKey = calculateCountByAgeAndBmiKey(profiles);
    List<BmiStat> stats = calculatePercentageByBmiCategory(countByAgeBmiKey, totalUsers);
    Age ageWithMostObese = findAgeWithMostObese(countByAgeBmiKey);
    return new BmiGlobalResult(totalUsers, ageWithMostObese, stats);
  }

  private Map<AgeBmiKey, Long> calculateCountByAgeAndBmiKey(List<PhysicalProfile> profiles) {
    return profiles.stream().collect(Collectors.groupingBy(
      p -> new AgeBmiKey(
        new Age((p.age().value() / 10) * 10), calculateBmiCategory(p)), Collectors.counting()
    ));
  }

  private List<BmiStat> calculatePercentageByBmiCategory(Map<AgeBmiKey, Long> countByAgeBmiKey,
    long totalUsers) {
    return countByAgeBmiKey.entrySet().stream().map(entry -> new BmiStat(
      entry.getKey().age(), entry.getKey()
      .category(), new Percentage(100.0 * entry.getValue() / totalUsers)
    )).toList();
  }

  private Age findAgeWithMostObese(Map<AgeBmiKey, Long> countByAgeBmiKey) {
    return countByAgeBmiKey.entrySet().stream()
      .filter(entry -> entry.getKey().category() == BmiCategory.OBESE)
      .max(Comparator.comparingLong(Map.Entry::getValue)).map(Map.Entry::getKey).map(AgeBmiKey::age)
      .orElse(null);
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

  private BmiCategory calculateBmiCategoryForChild(Gender gender, Age age, WeightInPounds w,
    HeightInInches h) {
    // Mock random BMI category for children
    var categories = BmiCategory.values();
    int randomIndex = ThreadLocalRandom.current().nextInt(categories.length);
    return categories[randomIndex];
  }

  public BmiCategory calculateBmiCategoryForAdult(WeightInPounds weight, HeightInInches height) {
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
      new PhysicalProfile(Gender.FEMALE, new Age(15), new WeightInPounds(100), new HeightInInches(60)),
      new PhysicalProfile(Gender.MALE, new Age(25), new WeightInPounds(176), new HeightInInches(70)),
      new PhysicalProfile(Gender.MALE, new Age(35), new WeightInPounds(260), new HeightInInches(69)),
      new PhysicalProfile(Gender.FEMALE, new Age(45), new WeightInPounds(150), new HeightInInches(65)),
      new PhysicalProfile(Gender.FEMALE, new Age(55), new WeightInPounds(190), new HeightInInches(63)),
      new PhysicalProfile(Gender.MALE, new Age(65), new WeightInPounds(240), new HeightInInches(70)));
    var results = service.computeBmiStats(profiles);
    log.info("Results {}", results);
  }
}
