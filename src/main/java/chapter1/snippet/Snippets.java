package chapter1.snippet;

import chapter1.domain.Price;
import chapter1.domain.PricingDetails;
import chapter1.domain.Product;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Supplementary snippets for Chapter 1.
 *
 * <p>This class contains small, self-contained code fragments used to illustrate
 * the contrast between imperative and functional approaches.</p>
 */
public class Snippets {
  public record User(Address address) {}
  public record Address(String city) {}

  /**
   * Demonstrates the traditional, imperative approach to null handling using
   * nested {@code if} statements.
   *
   * <p>This method retrieves the city name from a {@link chapter1.domain.User}
   * object by manually checking each level of the object graph for {@code null}.
   * If the user, address, or city is missing, it returns the default value
   * {@code "Unknown"}.</p>
   *
   * <p>While this pattern works correctly, it is verbose, error-prone, and
   * obscures the developer’s true intent (“get the user’s city”). It serves as
   * a pedagogical example of code that will be refactored into a cleaner
   * {@link java.util.Optional}-based version in {@link #getCityWithOptional(User)}.</p>
   *
   * @param user the {@link chapter1.domain.User} whose city should be retrieved;
   *             may be {@code null}
   * @return the city name if available, or {@code "Unknown"} if any intermediate
   *         property is {@code null}
   */
  public String getCityWithNullChecks(chapter1.domain.User user) {
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
   * {@link #getCityWithNullChecks(chapter1.domain.User)}, demonstrating how
   * functional composition improves readability, safety, and expressiveness.</p>
   *
   * @param user a {@link chapter1.domain.User} whose address and city may be {@code null}
   * @return the city name if present, otherwise {@code "Unknown"}
   */
  public String getCityWithOptional(User user) {
    return Optional.ofNullable(user)
      .map(User::address)
      .map(Address::city)
      .orElse("Unknown");
  }

  /**
   * Retrieves the discounted price of a {@link chapter1.domain.Product}
   * using explicit null checks at each level of the object graph.
   *
   * <p>This method illustrates the traditional imperative approach to
   * navigating nested objects. It returns the discounted price amount if
   * available, or {@code null} if any intermediate reference is missing.</p>
   *
   * <p>Although functionally correct, this pattern is verbose.
   * It hides the programmer’s intent (“get the discounted price”) behind a
   * maze of defensive conditions. In {@link #getDiscountedPriceWithOptional(Product)}, this logic will be
   * refactored into a concise and expressive {@link java.util.Optional}-based
   * version.</p>
   *
   * @param product the {@link chapter1.domain.Product} whose discounted price
   *                should be retrieved; may be {@code null}
   * @return the discounted {@link java.math.BigDecimal} amount, or {@code null}
   *         if the product or its pricing details are missing
   */
  public BigDecimal getDiscountedPriceWithNullChecks(Product product){
    BigDecimal price = null;
    if (product != null
      && product.pricingDetails() != null
      && product.pricingDetails().discountedPrice() != null) {
      price = product.pricingDetails().discountedPrice().amount();
    }
    return price;
  }

  /**
   * Retrieves the discounted price of a {@link chapter1.domain.Product}
   * using a fluent {@link java.util.Optional} pipeline.
   *
   * <p>This version expresses intent directly and eliminates explicit null
   * checks by chaining {@code map} operations through each layer of the
   * object graph. The result is wrapped in an {@link Optional}, which
   * clearly communicates that the value may be absent.</p>
   *
   * @param product the {@link chapter1.domain.Product} whose discounted price
   *                should be retrieved; may be {@code null}
   * @return an {@link Optional} containing the discounted
   *         {@link java.math.BigDecimal} amount if available,
   *         or {@link Optional#empty()} if the product or pricing data is missing
   */
  public Optional<BigDecimal> getDiscountedPriceWithOptional(Product product) {
    return Optional.ofNullable(product)
      .map(Product::pricingDetails)
      .map(PricingDetails::discountedPrice)
      .map(Price::amount);
  }
}
