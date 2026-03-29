package chapter4.listing43;

public record HeightInInches(double value) {
  public HeightInInches {
    if (value <= 0) {
      throw new IllegalArgumentException("Height must be greater " +
        "than 0 inches.");
    }
  }
}

