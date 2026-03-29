package chapter9.exercise.solution.rules;

import chapter9.exercise.solution.ShippingContext;
import chapter9.exercise.solution.ShippingType;

import java.math.BigDecimal;

public class ExpressFeeRule implements ShippingRule {
  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    return ShippingType.EXPRESS.equals(context.shippingType()) ? cost.add(new BigDecimal("10")) :
      cost;
  }
}