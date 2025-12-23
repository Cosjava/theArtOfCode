package chapter3.mock;

import chapter3.exception.EmailException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailService {
  public void send(String email, String message) throws EmailException {
    //mock implementation
    log.info("Message sent {} to {}", message, email);
  }
}
