package chapter5.exception;

import chapter5.domain.BookFormat;

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
