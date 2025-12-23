package chapter10.exercise1.solution.rules;

import chapter10.exercise1.solution.ShippingContext;
import chapter10.exercise1.solution.ShippingType;

import java.math.BigDecimal;

public class ExpressFeeRule implements ShippingRule {
  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    return ShippingType.EXPRESS.equals(context.shippingType()) ? cost.add(new BigDecimal("10")) :
      cost;
  }
}