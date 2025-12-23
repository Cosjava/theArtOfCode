package chapter10.mock;

import java.util.Collection;
import java.util.Collections;

public class OrderRepository {
  public Collection<Object> findByUserId(long id) {
    //mock
    return Collections.emptyList();
  }
}
