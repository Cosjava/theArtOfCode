package chapter10.listing101;

/**
 * A rectangular shape that correctly implements both {@code area}
 * and {@code perimeter}.
 *
 * <p>This implementation works within the flawed hierarchy only because a
 * rectangle naturally supports both operations.</p>
 */
public record Rectangle(double width, double height) implements Shape {
  @Override
  public Double area() {
    return width * height;
  }

  @Override
  public double perimeter(){
    return (width + height) * 2;
  }
}
