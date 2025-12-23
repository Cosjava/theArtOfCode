package chapter10.listing102;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

/**
 * Listing 10.2 – Validated Data Transfer Object.
 *
 * <p>This DTO illustrates how to enforce validation rules directly on incoming
 * data by using Jakarta Bean Validation annotations.</p>
 */
@Data
@Builder
public class UserRegistrationDTO {

  @NotBlank(message = "First name is required.")
  @Size(max = 50, message = "First name must be between 2 and 50 characters.")
  private String firstName;

  @NotBlank(message = "Last name is required.")
  @Size(max = 50, message = "Last name must be between 2 and 50 characters.")
  private String lastName;

  @NotBlank(message = "Email address is required.")
  @Email(message = "Email address must be valid.")
  private String email;

  @NotNull(message = "Age is required.")
  @Min(value = 13, message = "User must be at least 13 years old.")
  @Max(value = 130, message = "Age must be realistic.")
  private Integer age;

  @Pattern(regexp = "^(\\+1)?\\s*\\(?\\d{3}\\)?" +
    "[-\\s.]?\\d{3}[-\\s.]?\\d{4}$", message = "Phone " +
    "number must be a valid US number")
  private String phoneNumber;

  @Size(max = 200, message = "Description must be 200 characters maximum.")
  private String description;
}
