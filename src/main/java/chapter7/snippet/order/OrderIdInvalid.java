package chapter7.snippet.order;

public record OrderIdInvalid(String orderId, String reason)
  implements OrderIdValidationResult {}
