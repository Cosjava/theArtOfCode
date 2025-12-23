package chapter3.listing33;

import chapter3.domain.Customer;
import chapter3.exception.EmailException;
import chapter3.mock.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Listing 3.3 – Second refactoring: reducing cognitive complexity with streams.
 *
 * <p>This listing refactors the imperative version from <strong>listing 3.2</strong> into a
 * more declarative, functional style using the Java Streams API. The core
 * logic of sending promotional reminders remains identical, but the cognitive
 * load is further reduced by removing explicit loops and conditional nesting
 * .</p>
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
    customers.stream().filter(this::shouldSendReminder)
      .forEach(customer -> sendEmail(customer, promotionCode));
  }

  private void sendEmail(Customer customer, String promotionCode) {
    String message = buildReminderEmail(customer, promotionCode);
    try {
      emailService.send(customer.email(), message);
    } catch (EmailException exception) {
      log.error(String.format("Failed to send to email %s with " + "message %s",
        customer.email(), message), exception);
    }
  }

  private boolean shouldSendReminder(Customer customer) {
    return !StringUtils.isBlank(
      customer.email()) && customer.isActive() && (customer.hasOptIn() || customer.isGoldBuyer());
  }

  private String buildReminderEmail(Customer customer, String promotionCode) {
    var greeting = customer.isGoldBuyer() ? "As one of our valued Gold " +
      "members, we’ve prepared a "
      + "special offer just for you!" : "We miss you! It’s been a while since" +
      " your last purchase.";

    return """
      Hello %s,
      
      %s
      
      Use code %s to enjoy exclusive discounts on your next order.
      
      Hurry! The offer expires soon.
      
      Cheers,
      """.formatted(customer.email(), greeting, promotionCode);
  }

  static void main(String[] args) {
    List<chapter3.domain.Customer> customers = List.of(
      new chapter3.domain.Customer(
        "alice@example.com", LocalDate.now().minusMonths(2), true, false),
      // Active + opted-in
      new chapter3.domain.Customer(
        "bob@example.com", LocalDate.now().minusMonths(4), false, true),
      // Active + gold buyer
      new chapter3.domain.Customer(
        "carol@example.com", LocalDate.now().minusMonths(10), true, false),
      // Inactive
      new chapter3.domain.Customer(
        "", LocalDate.now().minusMonths(3), true, false)// Missing email
    );

    MarketingService service = new MarketingService(new EmailService());
    service.sendEmailReminders(customers, "SPRINGSALE2025");
  }

}
