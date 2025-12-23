package chapter10.exercise1.solution;

import chapter10.domain.Cart;
import chapter10.exercise1.User;
import chapter10.exercise1.solution.rules.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Solution - Refactoring for durability
 *
 * <p>This class provides a modular and extensible alternative to the
 * monolithic branching logic shown in the original {@code ShippingCalculator}.
 * Instead of embedding all pricing rules inside a single method, the solution
 * applies a list of independent {@link ShippingRule} objects to the shipping
 * cost. Each rule handles one concern, which aligns the design with the
 * Single Responsibility Principle.</p>
 */
@Slf4j
public class ShippingCalculatorSolution {

  /**
   * Calculates the final shipping cost by applying a sequence of independent
   * pricing rules.
   *
   * @param rules the list of pricing rules to apply, in the order they should run
   * @param context the shipping context containing user, date, and shipping type
   * @param cart the cart whose total provides the initial cost
   * @return the final shipping cost after all rules have been applied
   */
  public BigDecimal calculate(List<ShippingRule> rules, ShippingContext context, Cart cart) {
    BigDecimal cost = cart.total();
    for (ShippingRule rule : rules) {
      cost = rule.apply(context, cost);
    }
    return cost;
  }

  public static void main(String[] args) {
    List<ShippingRule> rules = List.of(new StandardRule(), new VipFreeShippingRule(),
      new ExpressFeeRule(), new HolidaySurchargeRule(), new CountryRule());
    ShippingCalculatorSolution calculator = new ShippingCalculatorSolution();

    Cart cart = new Cart(BigDecimal.valueOf(70));

    User userUS = new User("USA", false);
    User userFR = new User("FR", false);
    User userDE = new User("DE", false);
    User userSP = new User("SP", false);
    LocalDate now = LocalDate.now();
    LocalDate christmas = LocalDate.of(2026, Month.DECEMBER, 25);

    log.info("Domestic standard: " + calculator.calculate(rules, new ShippingContext(userUS,
      ShippingType.STANDARD, now), cart));
    log.info("Express domestic: " + calculator.calculate(rules, new ShippingContext(userUS,
      ShippingType.EXPRESS, now), cart));
    log.info("Holiday domestic: " + calculator.calculate(rules, new ShippingContext(userUS,
      ShippingType.STANDARD, christmas), cart));
    log.info("International France: " + calculator.calculate(rules, new ShippingContext(userFR,
      ShippingType.STANDARD, now), cart));
    log.info("International Germany (express + holiday): " + calculator.calculate(rules,
      new ShippingContext(userDE, ShippingType.EXPRESS, christmas), cart));
    log.info("Other international (default multiplier 2): " + calculator.calculate(rules,
      new ShippingContext(userSP, ShippingType.STANDARD, now), cart));
  }

}
