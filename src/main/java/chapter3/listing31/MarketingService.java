package chapter3.listing31;

import chapter3.domain.Customer;
import chapter3.exception.EmailException;
import chapter3.mock.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Listing 3.1 – Measuring cognitive complexity in a realistic method.
 *
 * <p>This listing introduces the concept of <em>cognitive complexity</em>—how
 * difficult code is to mentally follow, regardless of its correctness or
 * performance. The {@link #sendEmailReminders(java.util.List, String)} method
 * performs a familiar business task: sending promotional email reminders to
 * customers. Although the logic is valid, the nesting of conditions, combined
 * checks, and inline error handling make it cognitively dense.</p>
 *
 * <p>This code will be refactored in <strong>listing 3.2</strong> to reduce
 * cognitive complexity.</p>
 *
 * <p>This example uses mock services
 * ({@link chapter3.mock.EmailService}
 * to focus purely on readability and flow.</p>
 */
@Slf4j
public class MarketingService {
  private final EmailService emailService;

  public MarketingService(EmailService emailService) {
    this.emailService = emailService;
  }

  public void sendEmailReminders(
    List<Customer> customers, String promotionCode) {
    for (Customer customer : customers) {
      boolean isActiveCustomer = Optional.ofNullable(customer.lastPurchased())
        .filter(
          purchaseDate -> purchaseDate.isAfter(LocalDate.now().minusMonths(6)))
        .isPresent();

      if (!StringUtils.isBlank(
        customer.email()) && isActiveCustomer && (customer.hasOptIn() || customer.isGoldBuyer())) {
        String message = buildReminderEmail(customer, promotionCode);
        try {
          emailService.send(customer.email(), message);
        } catch (EmailException exception) {
          log.error(
            String.format("Failed to send email %s with " + "message %s",
              customer.email(), message), exception);
        }
      }
    }
  }

  private String buildReminderEmail(Customer customer, String promotionCode) {
    var greeting = customer.isGoldBuyer() ? "As one of our valued Gold " +
      "members, we’ve prepared a " +
      "special offer just for you!" : "We miss you! It’s been a while since " +
      "your last purchase.";

    return """
      Hello %s,
      
      %s
      
      Use code %s to enjoy exclusive discounts on your next order.
      
      Hurry! The offer expires soon.
      
      Cheers,
      """.formatted(customer.email(), greeting, promotionCode);
  }

  public static void main(String[] args) {
    List<Customer> customers = List.of(
      new Customer(
        "alice@example.com", LocalDate.now().minusMonths(2), true, false),
      // Active + opted-in
      new Customer(
        "bob@example.com", LocalDate.now().minusMonths(4), false, true),
      // Active + gold buyer
      new Customer("carol@example.com", LocalDate.now().minusMonths(10), true,
        false),
      // Inactive
      new Customer(
        "", LocalDate.now().minusMonths(3), true, false)// Missing email
    );

    MarketingService service = new MarketingService(new EmailService());
    service.sendEmailReminders(customers, "SPRINGSALE2025");
  }
}
