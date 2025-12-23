package chapter10.exercise1.solution.rules;

import chapter10.exercise1.solution.ShippingContext;

import java.math.BigDecimal;

public interface ShippingRule {
  BigDecimal apply(ShippingContext context, BigDecimal cartTotal);
}
