package chapter9.exercise.solution.rules;

import chapter9.exercise.solution.ShippingContext;

import java.math.BigDecimal;

public class StandardRule implements ShippingRule {
  @Override
  public BigDecimal apply(ShippingContext context, BigDecimal cartTotal) {
    return cartTotal.multiply(new BigDecimal("1.1"));
  }
}
