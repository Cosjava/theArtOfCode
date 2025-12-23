package chapter5.listing54;

public record Age(int value) {
  public Age {
    if (value < 0 || value > 150) {
      throw new IllegalArgumentException("Age must " +
        "be between 0 and 150.");
    }
  }
}

