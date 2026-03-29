package chapter7.listing71;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;

/**
 * Supplementary snippets for Chapter 7.
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

}
