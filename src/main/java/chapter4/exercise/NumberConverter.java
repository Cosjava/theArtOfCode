package chapter4.exercise;

import lombok.extern.slf4j.Slf4j;

/**
 * Exercise – Refactoring classic {@code instanceof} chains with pattern matching.
 *
 * <p>This exercise accompanies Chapter 4 and focuses on refactoring a
 * traditional {@code if/else} chain.</p>
 *
 * <p>The goal of this exercise is to demonstrate how pattern matching can
 * replace long conditional chains with cleaner and more declarative logic.</p>
 */
@Slf4j
public class NumberConverter {
  public int toInt(Number number) {
    if (number == null) {
      return -1;
    } else if (number instanceof Integer) {
      return (Integer) number;
    } else if (number instanceof Double) {
      return ((Double) number).intValue();
    } else if (number instanceof Float) {
      return ((Float) number).intValue();
    }
    throw new IllegalArgumentException("Unsupported number type: " + number.getClass()
      .getSimpleName());
  }

  public static void main(String[] args) {
    NumberConverter converter = new NumberConverter();

    log.info("Integer test: {}", converter.toInt(42));
    log.info("Double test: {}", converter.toInt(42.7));
    log.info("Float test: {}", converter.toInt(42.9f));
    log.info("Null test: {}", converter.toInt(null));

    try {
      log.info("Long test (unsupported): {}", converter.toInt(42L));
    } catch (IllegalArgumentException e) {
      log.error("Number converter error", e);
    }
  }
}



