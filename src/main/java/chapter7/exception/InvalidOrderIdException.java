package chapter7.exception;

public class InvalidOrderIdException extends Exception {
  private final String orderId;

  public InvalidOrderIdException(String orderId,
    String message) {
    super(message);
    this.orderId = orderId;
  }
}

