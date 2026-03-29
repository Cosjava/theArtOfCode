package chapter4.listing43;

public record Age(int value) {
  public Age {
    if (value < 0 || value > 150) {
      throw new IllegalArgumentException("Age must " +
        "be between 0 and 150.");
    }
  }
}

