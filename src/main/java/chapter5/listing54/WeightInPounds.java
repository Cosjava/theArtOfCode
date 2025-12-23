package chapter5.listing54;

public record WeightInPounds(double value) {
  public WeightInPounds {
    if (value <= 0) {
      throw new IllegalArgumentException("Weight must be greater " +
        "than 0 inches.");
    }
  }
}

