package chapter6.listing62;

import chapter6.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Listing 6.2 – Refactoring a Stream pipeline for clarity and expressiveness.
 *
 * <p>This listing refactors the verbose and difficult-to-read Stream pipeline
 * from {@link chapter6.snippet.Snippets#processUserEmails} into a cleaner,
 * more expressive version.</p>
 *
 * <ul>
 *   <li>{@link #isValidUser(User)} – determines whether a user should be
 *   included
 *       based on activity status, signup date, and email domain.</li>
 *   <li>{@link #getAndSanitizeEmail(User)} – retrieves and normalizes the
 *   user’s
 *       email address by converting it to lowercase and obfuscating the “@”
 *       symbol.</li>
 * </ul>
 */
@Slf4j
public class UserEmailService {

  private List<String> sortEmails(List<User> users) {
    return users
      .stream()
      .filter(this::isValidUser)
      .map(this::getAndSanitizeEmail)
      .sorted()
      .distinct()
      .limit(100)
      .toList();
  }

  private String getAndSanitizeEmail(User u) {
    return u
      .getEmail()
      .toLowerCase()
      .replace("@", "[at]");
  }

  private boolean isValidUser(User user) {
    return user.isActive() && user
      .getSignupDate()
      .isAfter(LocalDate
        .now()
        .minusYears(1)) && user
      .getEmail()
      .endsWith(".com");
  }

  public static void main(String[] args) {
    List<User> users = getUsersWithEmail();
    UserEmailService service = new UserEmailService();
    List<String> sortEmails = service.sortEmails(users);
    sortEmails.forEach(log::info);
  }

  public static List<User> getUsersWithEmail() {
    return Arrays.asList(User
      .builder()
      .email("alice@example.com")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusMonths(6))
      .build(), User
      .builder()
      .email("bob@sample.org")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusMonths(3))
      .build(), User
      .builder()
      .email("charlie@domain.com")
      .active(false)
      .signupDate(LocalDate
        .now()
        .minusMonths(2))
      .build(), User
      .builder()
      .email("dana@example.com")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusYears(2))
      .build(), User
      .builder()
      .email("eve@test.COM")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusMonths(1))
      .build(), User
      .builder()
      .email("frank@another.com")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusDays(10))
      .build(), User
      .builder()
      .email("alice@example.com")
      .active(true)
      .signupDate(LocalDate
        .now()
        .minusMonths(6))
      .build());
  }

}
