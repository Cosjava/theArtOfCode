package chapter1.listing12;

import chapter1.domain.Currency;
import chapter1.domain.Price;
import chapter1.domain.PricingDetails;
import chapter1.domain.Product;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Listing 1.2 – ProductService using {@link java.util.Optional}
 *
 * <p>This version refactors the imperative null-checking logic from Listing 1.1
 * into a more expressive and safer form using {@link Optional} and
 * {@link java.util.Objects}.</p>
 *
 * <p>Conceptually, this listing demonstrates the first step toward
 * eliminating raw nulls.
 * Later listings will push this further.</p>
 */
public class ProductService {

  public Optional<BigDecimal> getFinalPrice(Product product) {
    if (Objects.nonNull(product) && Objects.nonNull(product.pricingDetails())) {
      PricingDetails pricingDetails = product.pricingDetails();
      if (Objects.nonNull(pricingDetails.discountedPrice())) {
        return Optional.ofNullable(pricingDetails.discountedPrice().amount());
      } else if (Objects.nonNull(pricingDetails.basePrice())) {
        return Optional.ofNullable(
          product.pricingDetails().basePrice().amount());
      }
    }
    return Optional.empty();
  }

  public static void main(String[] args) {
    Price basePrice = new Price(Currency.DOLLARS, new BigDecimal("200.00"));
    Price discountedPrice = new Price(Currency.DOLLARS,
      new BigDecimal("150.00"));
    PricingDetails pricing = new PricingDetails(discountedPrice, basePrice);
    Product product = new Product("Laptop", pricing);
    ProductService productService = new ProductService();
    productService.getFinalPrice(product)
      .ifPresent(price -> System.out.println(product.name() + ":" + price));
  }
}
