package chapter4.snippet;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Supplementary snippets for Chapter 4 – Clarity of intent.
 *
 * <p>This class collects short, focused examples that illustrate core
 * principles
 * discussed in Chapter 4. The snippets contrast mutable and immutable code
 * and demonstrate how Java <em>records</em> can simplify data representation
 * .</p>
 */
@Slf4j
public class Snippets {
  /**
   * Demonstrates a common mutability problem when working with collections.
   *
   * <p>This method counts the number of products for a given country code
   * by removing elements from a list that do not start with the specified
   * prefix. However, instead of cloning the list properly, it assigns the
   * original reference to a new variable named {@code clone}. As a result,
   * the method mutates the original list passed as an argument.</p>
   *
   * @param products    the list of product identifiers
   * @param countryCode the prefix representing the country code to filter by
   * @return the number of products starting with the specified country code
   */
  public int countProductsByCountryCodeMutable(
    List<String> products, String countryCode) {
    List<String> clone = products;
    clone.removeIf(s -> !s.startsWith(countryCode));
    return clone.size();
  }

  /**
   * Counts products by country code without mutating the input list.
   *
   * <p>This refactored version fixes the earlier version
   * {@link #countProductsByCountryCodeMutable(List, String)}
   * that modified the input collection. It now uses a <em>stream pipeline</em>
   * to filter products declaratively and return the count of matching entries
   * without altering the original data.</p>
   *
   * @param products    the list of product identifiers to analyze (remains
   *                    unmodified)
   * @param countryCode the country code prefix used to filter products
   * @return the number of products whose identifiers start with the given
   * prefix
   */
  public long countProductsByCountryCode(
    List<String> products, String countryCode) {
    return products.stream()
      .filter(s -> s.startsWith(countryCode))
      .count();
  }

  public void testCountProductForCountryCode() {
    List<String> productsMutable = new ArrayList<>();
    productsMutable.add("FR-123");
    productsMutable.add("FR-999");
    productsMutable.add("US-789");
    int nbOfFrProductsMutable = countProductsByCountryCodeMutable(
      productsMutable,
      "FR-");
    log.info(
      "Number of french products (with mutable issue): {}",
      nbOfFrProductsMutable);
    int nbOfUSProductsMutable = countProductsByCountryCodeMutable(
      productsMutable,
      "US-");
    log.info(
      "Number of US products (with mutable issue): {}", nbOfUSProductsMutable);

    List<String> products = new ArrayList<>();
    products.add("FR-123");
    products.add("FR-999");
    products.add("US-789");
    long nbOfFrProducts = countProductsByCountryCode(products, "FR-");
    log.info("Number of french products: {}", nbOfFrProducts);
    long nbOfUSProducts = countProductsByCountryCode(products, "US-");
    log.info("Number of US products: {}", nbOfUSProducts);
  }

  public static void main(String[] args) {
    Snippets snippets = new Snippets();
    snippets.testCountProductForCountryCode();
  }

}
