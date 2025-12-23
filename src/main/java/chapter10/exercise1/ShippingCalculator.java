package chapter10.exercise1;

import chapter10.domain.Cart;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Exercise - Refactoring for durability
 *
 * <p>This class illustrates a problematic design for computing shipping costs.
 * The {@link #calculate(User, Cart, boolean, boolean, boolean)} method handles
 * multiple concerns at once and contains a growing number of conditional
 * branches.</p>
 */
@Slf4j
public class ShippingCalculator {
  public BigDecimal calculate(User user, Cart cart, boolean isExpress, boolean isHoliday,
    boolean isInternational) {
    BigDecimal base = cart.total().multiply(new BigDecimal("1.1"));
    if (isExpress) base = base.add(new BigDecimal("10"));
    if (isHoliday) base = base.multiply(new BigDecimal("1.2"));
    if (isInternational) {
      if (user.countryCode().equals("FR")) {
        base = base.multiply(new BigDecimal("1.4"));
      } else if (user.countryCode().equals("DE")) {
        base = base.multiply(new BigDecimal("1.6"));
      } else {
        base = base.multiply(new BigDecimal("2"));
      }
    }
    return base;
  }

  public static void main(String[] args) {
    ShippingCalculator calculator = new ShippingCalculator();

    Cart cart = new Cart(BigDecimal.valueOf(70));

    User userUS = new User("US", false);
    User userFR = new User("FR", false);
    User userDE = new User("DE", false);

    log.info("Domestic standard: " + calculator.calculate(userUS, cart, false, false, false));
    log.info("Express domestic: " + calculator.calculate(userUS, cart, true, false, false));
    log.info("Holiday domestic: " + calculator.calculate(userUS, cart, false, true, false));
    log.info("International France: " + calculator.calculate(userFR, cart, false, false, true));
    log.info("International Germany (express + holiday): " + calculator.calculate(userDE, cart,
      true, true, true));
    log.info("Other international (default multiplier 2): " + calculator.calculate(new User("ES",
      false), cart, false, false, true));
  }
}
