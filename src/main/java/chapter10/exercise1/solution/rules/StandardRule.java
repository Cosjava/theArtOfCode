package chapter10.exercise1.solution.rules;

import chapter10.exercise1.solution.ShippingContext;

import java.math.BigDecimal;

public class StandardRule implements ShippingRule {
  @Override
  public BigDecimal apply(ShippingContext context, BigDecimal cartTotal) {
    return cartTotal.multiply(new BigDecimal("1.1"));
  }
}
