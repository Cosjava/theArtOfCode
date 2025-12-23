package chapter3.domain;

import java.time.LocalDate;
import java.util.Optional;

public record Customer(String email, LocalDate lastPurchased, boolean hasOptIn,
                       boolean isGoldBuyer) {

  public boolean isActive() {
    return Optional.ofNullable(lastPurchased())
      .filter(purchaseDate -> purchaseDate.isAfter(LocalDate.now().minusMonths(6))).isPresent();
  }
}

