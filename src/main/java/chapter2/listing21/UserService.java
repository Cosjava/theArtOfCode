package chapter2.listing21;

import chapter2.domain.User;
import chapter2.exception.UserRegistrationException;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

/**
 * Listing 2.1 – A hard-to-read defensive implementation.
 *
 * <p>This listing intentionally demonstrates how long code with too many
 * chunks of logic—validation rules, exception handling, and raw database
 * operations—makes a program difficult to read and maintain.
 * Excessive nesting, repeated checks, and low-level JDBC calls
 * all distract from the main intent—registering a user.</p>
 * <p>
 * This example does not include a {@code main} method,
 * because running it would require setting up a database connection and schema
 * (e.g. a real or embedded database such as H2).
 */
public class UserService {

  public static User save(
    String firstName, String lastName, String email, LocalDate birthDate,
    String city) throws UserRegistrationException {
    if (lastName == null || lastName.isBlank()) {
      throw new UserRegistrationException("Last name is required");
    }

    if (birthDate == null) {
      throw new UserRegistrationException("Birth date is required");
    }

    if (birthDate.isAfter(LocalDate.now()) || (Period.between(birthDate,
        LocalDate.now())
      .getYears() > 140)) {
      throw new UserRegistrationException("Error on the birth date.");
    }

    if (email == null || email.isBlank()) {
      throw new UserRegistrationException("Email is required");
    }

    String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
    String insertSql = """
      INSERT INTO users (first_name, last_name, email, birth_date, city)
      VALUES (?, ?, ?, ?, ?)""";

    try (
      Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa",
        "")) {

      try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        checkStmt.setString(1, email);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
          throw new UserRegistrationException(
            "A user with this email " + "already exists");

        }
      }

      try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
        insertStmt.setString(1, firstName);
        insertStmt.setString(2, lastName);
        insertStmt.setString(3, email);
        insertStmt.setObject(4, birthDate);
        insertStmt.setString(5, city);
        insertStmt.executeUpdate();
      }

      return new User(firstName, lastName, email, birthDate, city);

    } catch (SQLException e) {
      throw new UserRegistrationException(
        "Database error during " + "user registration", e);
    }
  }
}
