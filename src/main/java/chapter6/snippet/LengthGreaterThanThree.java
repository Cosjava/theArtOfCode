package chapter6.snippet;

public class LengthGreaterThanThree implements StringPredicate {
  @Override
  public boolean test(String value) {
    return value.length() > 3;
  }
}

