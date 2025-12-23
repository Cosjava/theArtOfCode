package chapter1.listing11;

import chapter1.domain.Currency;
import chapter1.domain.Price;
import chapter1.domain.PricingDetails;
import chapter1.domain.Product;

import java.math.BigDecimal;

/**
 * Listing 1.1 – ProductService
 *
 * <p>Demonstrates a traditional imperative approach to retrieving a
 * product’s final price.
 * The method checks for null references at each level of the object graph
 * before accessing
 * nested values.</p>
 *
 * <p>In later examples (Listing 1.2 and beyond), this logic will be
 * refactored to a more elegant,
 * functional style using {@code Optional} chaining.</p>
 */
public class ProductService {

  public BigDecimal getFinalPrice(Product product) {
    if (product != null && product.pricingDetails() != null) {
      if (product.pricingDetails().discountedPrice() != null) {
        return product.pricingDetails().discountedPrice().amount();
      } else if (product.pricingDetails().basePrice() != null) {
        return product.pricingDetails().basePrice().amount();
      }
    }
    return null;
  }

  public static void main(String[] args) {
    Price basePrice = new Price(Currency.DOLLARS, new BigDecimal("200.00"));
    Price discountedPrice = new Price(Currency.DOLLARS,
      new BigDecimal("150.00"));
    PricingDetails pricing = new PricingDetails(discountedPrice, basePrice);
    Product product = new Product("Laptop", pricing);
    ProductService productService = new ProductService();
    BigDecimal price = productService.getFinalPrice(product);
    if (price != null) {
      System.out.println(product.name() + ":" + price);
    } else {
      System.out.println("No price");
    }
  }
}
