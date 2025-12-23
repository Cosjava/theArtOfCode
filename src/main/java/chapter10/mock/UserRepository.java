package chapter10.mock;

import chapter10.domain.User;
import chapter10.domain.UserSummary;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class UserRepository {
  public List<User> findAll() {
    //mock
    return Collections.emptyList();
  }

  public List<UserSummary> findActiveUsersWithOrderCount(Pageable pageable) {
    //mock
    return null;
  }
}
