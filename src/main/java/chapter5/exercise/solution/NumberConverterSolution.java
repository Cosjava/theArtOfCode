package chapter5.exercise.solution;

import lombok.extern.slf4j.Slf4j;

/**
 * Solution – Refactoring number conversion using pattern matching.
 *
 * <p>This class provides two refactored versions of the {@code NumberConverter}
 * exercise from Chapter 5. Both methods perform the same task—converting
 * a {@link Number} into an {@code int}—but use modern Java features to replace
 * traditional {@code instanceof} chains with cleaner, more declarative logic.</p>
 *
 * <p><strong>Methods included:</strong></p>
 * <ul>
 *   <li>{@link #convertUsingPatternMatching(Number)} –
 *       uses <em>pattern matching for {@code instanceof}</em> to merge
 *       type checks and variable binding in a single expression.</li>
 *   <li>{@link #convertUsingPatternMatchingSwitch(Number)} –
 *       further refactors the logic using a <em>switch expression</em>
 *       with type patterns for even greater readability and simplicity.</li>
 * </ul>
  */
@Slf4j
public class NumberConverterSolution {
  public int convertUsingPatternMatching(Number number) {
    if (number == null) {
      return -1;
    } else if (number instanceof Integer value) {
      return value;
    } else if (number instanceof Double value) {
      return value.intValue();
    } else if (number instanceof Float value) {
      return value.intValue();
    }
    throw new IllegalArgumentException("Unsupported number type: " + number.getClass()
      .getSimpleName());
  }

  public int convertUsingPatternMatchingSwitch(Number number) {
    return switch (number) {
      case null -> -1;
      case Integer value -> value;
      case Double value -> value.intValue();
      case Float value -> value.intValue();
      default -> throw new IllegalArgumentException("Unsupported number type: " + number.getClass()
        .getSimpleName());
    };
  }

  public static void main(String[] args) {
    NumberConverterSolution converter = new NumberConverterSolution();

    log.info("Integer test with instanceOf: {}", converter.convertUsingPatternMatching(42));
    log.info("Double test with instanceOf: {}", converter.convertUsingPatternMatching(42.7));
    log.info("Float test with instanceOf: {}", converter.convertUsingPatternMatching(42.9f));
    log.info("Null test with instanceOf: {}", converter.convertUsingPatternMatching(null));

    log.info("Integer test with switch: {}", converter.convertUsingPatternMatchingSwitch(42));
    log.info("Double test with switch: {}", converter.convertUsingPatternMatchingSwitch(42.7));
    log.info("Float test with switch: {}", converter.convertUsingPatternMatchingSwitch(42.9f));
    log.info("Null test with switch: {}", converter.convertUsingPatternMatchingSwitch(null));

    try {
      log.info("Long test (unsupported) with instanceOf: {}", converter.convertUsingPatternMatching(42L));
    } catch (IllegalArgumentException e) {
      log.error("Number converter error", e);
    }

    try {
      log.info("Long test (unsupported) with switch: {}", converter.convertUsingPatternMatchingSwitch(42L));
    } catch (IllegalArgumentException e) {
      log.error("Number converter error", e);
    }
  }
}
