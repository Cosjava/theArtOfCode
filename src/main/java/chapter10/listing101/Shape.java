package chapter10.listing101;

/**
 * Listing 10.1 – Problematic hierarchical design.
 *
 * <p>This example shows a hierarchy where the abstraction is too general.
 * The interface requires all shapes to provide an area and a
 * perimeter, which forces implementations that do not logically support
 * one of these operations to return meaningless values.</p>
 */
public interface Shape {
  Double area();
  double perimeter();
}
