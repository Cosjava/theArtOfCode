package chapter1.snippet;

import chapter1.domain.Product;
import lombok.Getter;

@Getter
public class MissingPriceException extends Exception {
  private final Product product;

  public MissingPriceException(Product product) {
    super(buildMessage(product));
    this.product = product;
  }

  private static String buildMessage(Product product) {
    return "Missing price for product: " + product;
  }
}
