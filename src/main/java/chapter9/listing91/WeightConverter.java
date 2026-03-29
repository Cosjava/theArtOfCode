package chapter9.listing91;

public class WeightConverter {
  private static final double POUND_TO_KILO = 0.45359237;

  /**
   * Converts pounds to kilograms.
   *
   * @param pounds the weight in pounds, must be non-negative
   * @return the weight converted to kilograms
   * @throws IllegalArgumentException if pounds is negative
   */
  public double poundsToKilos(double pounds) {
    if (pounds < 0) {
      throw new IllegalArgumentException("Weight must be non-negative.");
    }
    return pounds * POUND_TO_KILO;
  }
}
