package chapter10.listing101;

/**
 * A line segment forced into the {@code Shape} hierarchy even though
 * it cannot provide a meaningful area.
 *
 * <p>Returning {@code null} for {@code area} (#A) illustrates how an
 * overly broad interface breaks substitutability.
 * This is a classic example of a design that violates
 * the Liskov Substitution Principle.</p>
 */
public record Line(double length) implements Shape {
  @Override
  public Double area() {
    return null;
  }

  @Override
  public double perimeter(){
    return length;
  }
}
