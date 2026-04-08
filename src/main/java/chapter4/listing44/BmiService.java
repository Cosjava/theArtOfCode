package chapter4.listing44;

import chapter4.domain.BmiCategory;
import chapter4.listing43.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Listing 4.4 – Computing BMI statistics using records, streams, and pattern matching.
 *
 * <p>The {@link BmiService} computes body mass index (BMI)
 * categories for a collection of {@link chapter4.listing43.PhysicalProfile} instances
 * and aggregates statistical insights by age group and category.</p>
 *
 * <p>While the implementation is technically correct, it lacks clarity.
 * <strong>Listing 4.5</strong> will refactor this version to reveal its true
 * intent.</p>
 */
@Slf4j
public class BmiService {
  public List<Object> computeBmiStats(
    List<PhysicalProfile> profiles) {
    long totalUsers = profiles.size();
    Map<Integer, Map<BmiCategory, Long>> countByAgeAndBmi = profiles.stream()
      .collect(Collectors.groupingBy(
        p -> (p.age()
          .value() / 10) * 10, Collectors.groupingBy(
          this::calculateBmiCategory,
          Collectors.counting())
      ));
    Integer ageWithMostObese = countByAgeAndBmi.entrySet().stream()
      .filter(e -> e.getValue().containsKey(BmiCategory.OBESE))
      .max(Comparator.comparingLong(e -> e.getValue().getOrDefault(BmiCategory.OBESE, 0L)))
      .map(Map.Entry::getKey).orElse(null);

    List<Object> stats = new ArrayList<>();
    countByAgeAndBmi.forEach((age, counts) -> counts.forEach((category, count) -> {
      double percentage = totalUsers == 0
        ? 0.0
        : 100.0 * count / totalUsers;
      stats.add(new Object[]{age, category, percentage});
    }));

    List<Object> result = new ArrayList<>();
    result.add(totalUsers);
    result.add(ageWithMostObese);
    result.add(stats);
    return result;
  }

  public BmiCategory calculateBmiCategory(PhysicalProfile profile) {
    return switch (profile) {
      case PhysicalProfile(
        _,
        Age age,
        WeightInPounds w,
        HeightInInches h
      ) when age.value() >= 18 -> calculateBmiCategoryForAdult(w, h);
      case PhysicalProfile(
        Gender gender,
        Age age,
        WeightInPounds w,
        HeightInInches h
      ) -> calculateBmiCategoryForChild(gender, age, w, h);
    };
  }

  private BmiCategory calculateBmiCategoryForChild(Gender gender, Age age, WeightInPounds w, HeightInInches h) {
    // Mock random BMI category for children
    var categories = BmiCategory.values();
    int randomIndex = ThreadLocalRandom.current().nextInt(categories.length);
    return categories[randomIndex];
  }

  public BmiCategory calculateBmiCategoryForAdult(
    WeightInPounds weight,
    HeightInInches height
  ) {
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
