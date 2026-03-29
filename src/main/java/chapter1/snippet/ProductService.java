package chapter1.snippet;

import chapter1.domain.Currency;
import chapter1.domain.Price;
import chapter1.domain.PricingDetails;
import chapter1.domain.Product;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>This class shows how to combine fluent {@link java.util.Optional}
 * pipelines with domain-specific
 * exception handling.</p>
 */
@Slf4j
public class ProductService {

  /**
   * Computes the product’s final price using a fluent {@link Optional}
   * pipeline.
   *
   * <p>The method extracts pricing details, attempts to resolve a discounted
   * price first, then falls back to a base price. If no price is available,
   * it throws a domain-specific {@link MissingPriceException}.</p>
   *
   * @param product the product whose price should be computed
   * @return the resolved price as a {@link BigDecimal}
   * @throws MissingPriceException if neither discounted nor base price is
   *                               present
   */
  public BigDecimal getFinalPrice(Product product)
    throws MissingPriceException {
    return Optional.ofNullable(product)
      .map(Product::pricingDetails)
      .map(this::selectPrice)
      .map(Price::amount)
      .orElseThrow(() -> new MissingPriceException(product));
  }

  /**
   * Resolves the price within the provided {@link PricingDetails}.
   *
   * <p>This method encapsulates the rule: prefer the discounted price when
   * present, otherwise use the base price. The base price must not be null.
   * It returns an {@link Optional}.</p>
   *
   * @param details pricing information containing discounted and base values
   * @return an Optional containing the chosen price, or empty if none is
   * present
   */
  private Price selectPrice(PricingDetails details) {
    return Objects.requireNonNullElse(
      details.discountedPrice(),
      details.basePrice());
  }

  public static void main(String[] args) throws MissingPriceException {
    Price basePrice = new Price(Currency.DOLLARS, new BigDecimal("200.00"));
    Price discountedPrice = new Price(Currency.DOLLARS,
      new BigDecimal("150.00"));
    PricingDetails pricing = new PricingDetails(discountedPrice, basePrice);
    Product product = new Product("Laptop", pricing);
    ProductService productService = new ProductService();
    BigDecimal finalPrice = productService.getFinalPrice(product);
    log.info("Final Price: {}", finalPrice);
  }
}
