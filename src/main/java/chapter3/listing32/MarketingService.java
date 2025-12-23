package chapter3.listing32;

import chapter3.domain.Customer;
import chapter3.exception.EmailException;
import chapter3.mock.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Listing 3.2 – First refactoring to reduce cognitive complexity.
 *
 * <p>This listing refactors the <strong>listing 3.1</strong>. The goal is to reduce cognitive
 * complexity without changing behavior.</p>
 *
 * <p>The {@link #sendEmailReminders(java.util.List, String)} method still loops
 * through all customers, but its decision logic has been extracted into
 * {@link #shouldSendReminder(chapter3.domain.Customer)}, and the email-sending
 * logic has been moved to {@link #sendEmail(chapter3.domain.Customer, String)}.
 * Each method now communicates intent through its name, allowing readers to
 * understand the workflow at a glance.</p>
 *
 * <p>A second refactoring in <strong>listing 3.3</strong> will take this one
 * step further by using the <em>Streams API</em> to express the same logic in a
 * more declarative style, further simplifying the control flow.</p>
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
      if (shouldSendReminder(customer)) {
        sendEmail(customer, promotionCode);
      }
    }
  }

  private void sendEmail(Customer customer, String promotionCode) {
    String message = buildReminderEmail(customer, promotionCode);
    try {
      emailService.send(customer.email(), message);
    } catch (EmailException exception) {
      log.error(String.format("Failed to send to email %s with " + "message %s",
        customer.email()
        , message), exception);
    }
  }

  private boolean shouldSendReminder(Customer customer) {
    return !StringUtils.isBlank(
      customer.email()) && customer.isActive() && (customer.hasOptIn() || customer.isGoldBuyer());
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
