package chapter9.exercise.solution.rules;

import chapter9.exercise.solution.ShippingContext;

import java.math.BigDecimal;

public class CountryRule implements ShippingRule {
  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    String country = context.user().countryCode();
    if ("USA".equals(country)) {
      return cost;
    }
    return switch (country) {
      case "FR" -> cost.multiply(new BigDecimal("1.4"));
      case "DE" -> cost.multiply(new BigDecimal("1.6"));
      default -> cost.multiply(new BigDecimal("2"));
    };
  }
}
