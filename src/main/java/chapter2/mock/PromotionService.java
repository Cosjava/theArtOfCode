package chapter2.mock;

import chapter2.domain.Promotion;
import chapter2.domain.User;

import java.util.List;

public class PromotionService {
  public List<Promotion> getPromotionsFor(User user) {
    //mock implementation
    return List.of(new Promotion("50% Off All Items",
      "Celebrate your day with half-price deals!"),
      new Promotion("Free Shipping", "Enjoy free " +
      "delivery on all orders today!"));
  }
}
