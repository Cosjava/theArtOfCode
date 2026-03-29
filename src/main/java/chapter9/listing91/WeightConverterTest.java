package chapter9.listing91;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Listing 9.1 – Unit tests for the {@code WeightConverter} class.
 *
 * <p>This test class demonstrates how to write clear and expressive unit
 * tests using JUnit 5.</p>
 *
 */
class WeightConverterTest {

  private final WeightConverter converter = new WeightConverter();

  @Test
  void poundsToKilosValidValueReturnsConvertedWeight() {
    double result = converter.poundsToKilos(10);
    assertEquals(4.5359237, result, 0.000001);
  }

  @Test
  void poundsToKilosZeroRturnsZero() {
    assertEquals(0.0, converter.poundsToKilos(0));
  }

  @Test
  void poundsToKilosNegativeThrowsException() {
    assertThrows(IllegalArgumentException.class,
      () -> converter.poundsToKilos(-5));
  }
}

