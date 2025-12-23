package chapter2.listing27;

import chapter2.domain.Promotion;
import chapter2.domain.User;
import chapter2.mock.PromotionService;
import chapter2.mock.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * Listing 2.7 – A refined scene-level method that eliminates redundancy.
 *
 * <p>This listing refactors the previous implementation from listing 2.6,
 * simplifying the construction of promotional email content while preserving
 * the same narrative clarity. The <em>scene-level</em> method still represents
 * a focused responsibility—building the message body for a marketing email—
 * but now tells its story with less repetition and greater fluency.</p>
 *
 * <p>This example uses mock services
 * ({@link chapter2.mock.UserService} and
 * {@link chapter2.mock.PromotionService})
 * to focus purely on readability and flow.</p>
 */
@Slf4j
public class MarketingEmailService {
  private final UserService userService;
  private final PromotionService promotionService;

  public MarketingEmailService(UserService userService, PromotionService promotionService) {
    this.userService = userService;
    this.promotionService = promotionService;
  }

  public String buildPromotionalEmailContent(long userId) {
    User user = userService.findUserById(userId);
    List<Promotion> promotions = promotionService.getPromotionsFor(user);
    if (isToday(user.birthDate())) {
      return birthdayPromotionEmailFor(user, promotions);
    }
    return regularPromotionEmailFor(user, promotions);
  }

  private boolean isToday(LocalDate date) {
    if (date == null) {
      return false;
    }
    LocalDate today = LocalDate.now();
    return date.getMonth() == today.getMonth() && date.getDayOfMonth() == today.getDayOfMonth();
  }

  private String birthdayPromotionEmailFor(User user, List<Promotion> promotions) {
    var promotionLines = promotions.stream()
      .map(promo -> "• %s — %s".formatted(promo.title(), promo.description())).toList();

    return """
      🎉 Happy Birthday, %s! 🎂
      
      We’ve got some special birthday offers just for you:
      %s
      
      Enjoy your special day!
      """.formatted(user.firstName(), String.join("\n", promotionLines));
  }

  private String regularPromotionEmailFor(User user, List<Promotion> promotions) {
    var promotionLines = promotions.stream()
      .map(promo -> "• %s — %s".formatted(promo.title(), promo.description())).toList();

    return """
      Hello %s,
      
      Check out our latest promotions:
      %s
      
      Don’t miss out!
      """.formatted(user.firstName(), String.join("\n", promotionLines));
  }

  public static void main(String[] args) {
    MarketingEmailService emailService = new MarketingEmailService(new UserService(),
      new PromotionService());
    String emailContent = emailService.buildPromotionalEmailContent(1L);
    log.info(emailContent);
  }
}
