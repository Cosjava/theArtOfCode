package chapter7.snippet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This snippet supports Chapter 7, <em>Handling failure with grace</em>,
 * by illustrating how to use the SLF4J API to log information, debug data,
 * and errors consistently across different logging levels.
 *
 * <p>The {@link #processData(String)} method logs the input being processed,
 * attempts to parse it as an integer, and records both successful and failed
 * parsing outcomes using appropriate log levels:
 * <ul>
 *   <li>{@code info} – to log high-level operational messages</li>
 *   <li>{@code debug} – to log detailed diagnostic data during successful operations</li>
 *   <li>{@code error} – to log exceptions with contextual details when failures occur</li>
 * </ul>
 */
public class LogExample {
  private static final Logger logger = LoggerFactory.getLogger(
    LogExample.class);

  public void processData(String input) {
    logger.info("Processing input: {}", input);

    try {
      int value = Integer.parseInt(input);
      logger.debug("Parsed value: {}", value);
    } catch (NumberFormatException e) {
      logger.error("Invalid number format for input: {}", input, e);
    }
  }
}

