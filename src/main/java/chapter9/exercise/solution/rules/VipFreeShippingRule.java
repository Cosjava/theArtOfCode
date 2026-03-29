package chapter9.exercise.solution.rules;

import chapter9.exercise.solution.ShippingContext;

import java.math.BigDecimal;

public class VipFreeShippingRule implements ShippingRule {
  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    return context.user().isVip() ? BigDecimal.ZERO : cost;
  }
}