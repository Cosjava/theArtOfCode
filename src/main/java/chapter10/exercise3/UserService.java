package chapter10.exercise3;

import chapter10.domain.User;
import chapter10.domain.UserSummary;
import chapter10.mock.OrderRepository;
import chapter10.mock.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Exercise - Optimize code
 *
 * <p>This exercise presents an implementation that retrieves all active users,
 * fetches each user's orders individually, and builds a list of summary objects
 * sorted by order count. Although the method is functionally correct, it
 * contains several performance issues that become significant as the dataset
 * grows.</p>
 *
 */
public class UserService {
  private final UserRepository userRepository = new UserRepository();
  private final OrderRepository orderRepository = new OrderRepository();

  public List<UserSummary> listActiveUsers() {

    List<User> users = userRepository.findAll();

    List<UserSummary> result = new ArrayList<>();

    for (User user : users) {
      if (user.isActive()) {
        int orderCount = orderRepository
          .findByUserId(user.id()).size();
        result.add(new UserSummary(user.name(), orderCount));
      }
    }

    result.sort(Comparator.comparingInt(UserSummary::orderCount)
      .reversed());
    return result;
  }

}
