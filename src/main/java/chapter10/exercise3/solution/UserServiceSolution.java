package chapter10.exercise3.solution;

import chapter10.domain.UserSummary;
import chapter10.mock.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Solution - Optimize code
 *
 * <p>This service provides an optimized version of the {@code listActiveUsers}
 * method. Instead of loading all users and issuing one query per user to
 * retrieve order counts, this implementation delegates the work to the
 * repository layer using a single paginated query.</p>
 *
 */
public class UserServiceSolution {

  private final UserRepository userRepository = new UserRepository();

  public List<UserSummary> listActiveUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size,
      Sort.by("orders").descending());
    return userRepository.findActiveUsersWithOrderCount(pageable);
  }
}
