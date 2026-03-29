package chapter9.exercise.solution.rules;

import chapter9.exercise.solution.ShippingContext;

import java.math.BigDecimal;

public interface ShippingRule {
  BigDecimal apply(ShippingContext context, BigDecimal cartTotal);
}
