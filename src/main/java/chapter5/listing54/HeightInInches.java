package chapter5.listing54;

public record HeightInInches(double value) {
  public HeightInInches {
    if (value <= 0) {
      throw new IllegalArgumentException("Height must be greater " +
        "than 0 inches.");
    }
  }
}

