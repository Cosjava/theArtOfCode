package chapter7.exception;

public class DataProcessingException extends Exception {
  private final String data;

  public DataProcessingException(String data,
    String message, Exception exception) {
    super(message, exception);
    this.data = data;
  }
}

