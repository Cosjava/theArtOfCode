package chapter2.exception;

import lombok.Getter;

import java.sql.SQLException;

@Getter
public class UserRegistrationException extends Exception {
  public UserRegistrationException(String message) {
    super(message);
  }

  public UserRegistrationException(String message, SQLException e) {
    super(message, e);
  }
}
