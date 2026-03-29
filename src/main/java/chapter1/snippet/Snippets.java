package chapter1.snippet;

import chapter1.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Supplementary snippets for Chapter 1.
 *
 * <p>This class contains small, self-contained code fragments used to
 * illustrate
 * the contrast between imperative and functional approaches.</p>
 */
public class Snippets {

  /**
   * Demonstrates the traditional, imperative approach to null handling using
   * nested {@code if} statements.
   *
   * <p>This method retrieves the city name from a {@link User}
   * object by manually checking each level of the object graph for {@code
   * null}.
   * If the user, address, or city is missing, it returns the default value
   * {@code "Unknown"}.</p>
   *
   * <p>While this pattern works correctly, it is verbose, error-prone, and
   * obscures the developer’s true intent (“get the user’s city”). It serves as
   * a pedagogical example of code that will be refactored into a cleaner
   * {@link java.util.Optional}-based version in
   * {@link #getCityWithOptional(User)}.</p>
   *
   * @param user the {@link User} whose city should be retrieved;
   *             may be {@code null}
   * @return the city name if available, or {@code "Unknown"} if any
   * intermediate
   * property is {@code null}
   */
  public String getCityWithNullChecks(User user) {
    String city = null;
    if (user != null && user.getAddress() != null
      && user.getAddress().getCity() != null) {
      city = user.getAddress().getCity();
    } else {
      city = "Unknown";
    }
    return city;
  }

  /**
   * Demonstrates how to safely extract a city name from a nested object graph
   * using {@link Optional} chaining.
   *
   * <p>Equivalent to multiple explicit null checks, but written in a concise
   * and declarative style. Returns a default value when any link in the chain
   * is missing.</p>
   *
   * <p>This refactored version contrasts with
   * {@link #getCityWithNullChecks(User)}, demonstrating how
   * functional composition improves readability, safety, and expressiveness
   * .</p>
   *
   * @param user a {@link User} whose address and city may be {@code null}
   * @return the city name if present, otherwise {@code "Unknown"}
   */
  public String getCityWithOptional(User user) {
    return Optional.ofNullable(user)
      .map(User::getAddress)
      .map(Address::getCity)
      .orElse("Unknown");
  }


  /**
   * Retrieves the discounted price of a {@link Product}
   * using a fluent {@link java.util.Optional} pipeline.
   *
   * <p>This version expresses intent directly and eliminates explicit null
   * checks by chaining {@code map} operations through each layer of the
   * object graph. The result is wrapped in an {@link Optional}, which
   * clearly communicates that the value may be absent.</p>
   *
   * @param product the {@link Product} whose discounted price
   *                should be retrieved; may be {@code null}
   * @return an {@link Optional} containing the discounted
   * {@link java.math.BigDecimal} amount if available,
   * or {@link Optional#empty()} if the product or pricing data is missing
   */
  public Optional<BigDecimal> getDiscountedPriceWithOptional(Product product) {
    return Optional.ofNullable(product)
      .map(Product::pricingDetails)
      .map(PricingDetails::discountedPrice)
      .map(Price::amount);
  }
}
