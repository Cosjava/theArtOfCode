package chapter2.mock;

import chapter2.domain.User;

import java.time.LocalDate;

public class UserService {
  public User findUserById(long userId) {
    //mock implementation
    return new User("Bob", "Robert", "bob.robert@test.com", LocalDate.now(), "Paris");
  }
}
