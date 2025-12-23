package chapter10.exercise1.solution.rules;

import chapter10.exercise1.solution.ShippingContext;

import java.math.BigDecimal;
import java.time.MonthDay;
import java.util.Set;

public class HolidaySurchargeRule implements ShippingRule {
  private final Set<MonthDay> holidays = Set.of(
    MonthDay.of(12, 25),
    MonthDay.of(1, 1)
  );

  public BigDecimal apply(ShippingContext context, BigDecimal cost) {
    MonthDay today = MonthDay.from(context.date());
    return holidays.contains(today)
      ? cost.multiply(new BigDecimal("1.2"))
      : cost;
  }
}