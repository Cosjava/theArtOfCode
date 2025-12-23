package chapter4.exception;

import chapter4.domain.BookFormat;

public class UnsupportedBookFormatException extends Exception {
  private final BookFormat format;

  public UnsupportedBookFormatException(BookFormat format) {
    super(buildMessage(format));
    this.format = format;
  }

  private static String buildMessage(BookFormat format) {
    return "Unsupported book format: " + format;
  }

}
