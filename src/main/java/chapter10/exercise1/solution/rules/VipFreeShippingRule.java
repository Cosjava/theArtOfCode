package chapter10.exercise1.solution.rules;

import chapter10.exercise1.solution.ShippingContext;

import java.math.BigDecimal;

public class VipFreeShippingRule implements ShippingRule {
  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    return context.user().isVip() ? BigDecimal.ZERO : cost;
  }
}