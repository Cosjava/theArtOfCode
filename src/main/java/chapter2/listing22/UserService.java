package chapter2.listing22;

import chapter2.domain.User;
import chapter2.exception.UserRegistrationException;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;


/**
 * Listings 2.2, 2.3, 2.4 and 2.5 – Breaking long code into readable, focused methods.
 *
 * <p>This listing shows how to transform the long and defensive implementation
 * from listing 2.1 into shorter, self-contained methods using a narrative code
 * approach. The logic is identical, but readability and maintainability
 * are improved through proper method decomposition and
 * meaningful naming.</p>
 *
 * This example does not include a {@code main} method,
 * because running it would require setting up a database connection and schema
 * (e.g. a real or embedded database such as H2).
 */
public class UserService {

  public void handleCompleteUserRegistration(
    String firstName,
    String lastName,
    String email,
    LocalDate birthDate,
    String city) throws UserRegistrationException {
    User newUser = createUser(firstName, lastName, email, birthDate, city);
    sendPromotionalEmail(newUser);
    notifyCustomerService(newUser);
  }

  public User createUser(String firstName, String lastName, String email, LocalDate birthDate,
    String city) throws UserRegistrationException {
    validateMandatoryField(lastName, "Last name");
    validateBirthDate(birthDate);
    validateEmail(email);
    return saveUser(firstName, lastName, email, birthDate, city);
  }

  private void validateMandatoryField(String field, String fieldName) throws UserRegistrationException {
    if (StringUtils.isBlank(field)) {
      throw new UserRegistrationException(fieldName + " is required");
    }
  }

  private void validateBirthDate(LocalDate birthDate) throws UserRegistrationException {
    if (birthDate == null) {
      throw new UserRegistrationException("Birth date is required");
    }
    if (birthDate.isAfter(LocalDate.now()) || (Period.between(birthDate, LocalDate.now())
      .getYears() > 140)) {
      throw new UserRegistrationException("Error on the birth date.");
    }
  }

  private void validateEmail(String email) throws UserRegistrationException {
    validateMandatoryField(email, "E-mail");
    checkEmailUniqueness(email);
  }

  private void checkEmailUniqueness(String email) throws UserRegistrationException {
    String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
    try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", ""); PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
      checkStmt.setString(1, email);
      ResultSet rs = checkStmt.executeQuery();
      if (rs.next() && rs.getInt(1) > 0) {
        throw new UserRegistrationException("A user with this email " + "already exists");
      }
    } catch (SQLException e) {
      throw new UserRegistrationException("Database error during " + "user registration", e);
    }
  }

  public User saveUser(String firstName, String lastName, String email, LocalDate birthDate,
    String city) throws UserRegistrationException {
    String insertSql = """
      INSERT INTO users (first_name, last_name, email, birth_date, city)
      VALUES (?, ?, ?, ?, ?)""";

    try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", ""); PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
      insertStmt.setString(1, firstName);
      insertStmt.setString(2, lastName);
      insertStmt.setString(3, email);
      insertStmt.setObject(4, birthDate);
      insertStmt.setString(5, city);
      insertStmt.executeUpdate();
      return new User(firstName, lastName, email, birthDate, city);
    } catch (SQLException e) {
      throw new UserRegistrationException("Database error during " + "user registration", e);
    }
  }

  private void notifyCustomerService(User newUser) {
    //mock
  }

  private void sendPromotionalEmail(User newUser) {
    //mock
  }
}
