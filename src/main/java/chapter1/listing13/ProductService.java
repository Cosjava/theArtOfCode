package chapter1.listing13;

import chapter1.domain.Currency;
import chapter1.domain.Price;
import chapter1.domain.PricingDetails;
import chapter1.domain.Product;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Listing 1.3 – ProductService using functional composition
 *
 * <p>This version completes the evolution started in Listings 1.1 and 1.2,
 * replacing nested null checks and conditional logic with a clean,
 * declarative {@link java.util.Optional} pipeline.</p>
 *
 */
public class ProductService {

  public Optional<BigDecimal> getFinalPrice(Product product) {
    return Optional.ofNullable(product).map(Product::pricingDetails)
      .flatMap(this::priceAmount);
  }

  private Optional<BigDecimal> priceAmount(PricingDetails details) {
    return Optional.ofNullable(details.discountedPrice())
      .or(() -> Optional.ofNullable(details.basePrice())).map(Price::amount);
  }

  public static void main(String[] args) {
    Price basePrice = new Price(Currency.DOLLARS, new BigDecimal("200.00"));
    Price discountedPrice = new Price(Currency.DOLLARS,
      new BigDecimal("150.00"));
    PricingDetails pricing = new PricingDetails(discountedPrice, basePrice);
    Product product = new Product("Laptop", pricing);
    ProductService productService = new ProductService();
    Optional<BigDecimal> finalPrice = productService.getFinalPrice(product);
    finalPrice.ifPresentOrElse(
      price -> System.out.println(product.name() + ":" + price),
      () -> System.out.println("No price"));
  }
}
