package chapter2.listing26;

import chapter2.domain.Promotion;
import chapter2.domain.User;
import chapter2.mock.PromotionService;
import chapter2.mock.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

/**
 * Listing 2.6 – A scene-level method that constructs promotional email content.
 *
 * <p>The {@link #buildPromotionalEmailContent(long)} <em>scene-level</em>
 * method represents a step within a broader “delivering”
 * plot—the system preparing information to communicate with the outside
 * world.</p>>
 *
 * <p>While this version reads clearly and follows a natural flow—find the user,
 * check whether it’s their birthday, get the appropriate promotions, and build
 * the message—it still isn’t ideal. It introduces unnecessary local variables
 * and repeats logic that could be expressed more concisely.
 * Listing 2.7 will refactor this code to remove duplication and improve clarity
 * without altering behavior.</p>
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

  public MarketingEmailService(
    UserService userService, PromotionService promotionService) {
    this.userService = userService;
    this.promotionService = promotionService;
  }

  public String buildPromotionalEmailContent(long userId) {
    String emailContent;
    User user = userService.findUserById(userId);
    boolean isUserBirthday = isToday(user.birthDate());
    if (isUserBirthday) {
      List<Promotion> birthdayPromotions = promotionService.getPromotionsFor(
        user);
      emailContent = birthdayPromotionEmailFor(user, birthdayPromotions);
    } else {
      List<Promotion> standardPromotions = promotionService.getPromotionsFor(
        user);
      emailContent = regularPromotionEmailFor(user, standardPromotions);
    }
    return emailContent;
  }

  private boolean isToday(LocalDate date) {
    if (date == null) {
      return false;
    }
    LocalDate today = LocalDate.now();
    return date.getMonth() == today.getMonth() && date.getDayOfMonth() == today.getDayOfMonth();
  }

  private String birthdayPromotionEmailFor(
    User user, List<Promotion> promotions) {
    var promotionLines = promotions.stream()
      .map(promo -> "• %s — %s".formatted(promo.title(), promo.description()))
      .toList();

    return """
      🎉 Happy Birthday, %s! 🎂
      
      We’ve got some special birthday offers just for you:
      %s
      
      Enjoy your special day!
      """.formatted(user.firstName(), String.join("\n", promotionLines));
  }

  private String regularPromotionEmailFor(
    User user, List<Promotion> promotions) {
    var promotionLines = promotions.stream()
      .map(promo -> "• %s — %s".formatted(promo.title(), promo.description()))
      .toList();

    return """
      Hello %s,
      
      Check out our latest promotions:
      %s
      
      Don’t miss out!
      """.formatted(user.firstName(), String.join("\n", promotionLines));
  }

  public static void main(String[] args) {
    MarketingEmailService emailService = new MarketingEmailService(
      new UserService(),
      new PromotionService());
    String emailContent = emailService.buildPromotionalEmailContent(1L);
    log.info(emailContent);
  }
}
