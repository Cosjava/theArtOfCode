package chapter7.snippet;

import chapter7.exception.DataProcessingException;
import chapter7.exception.InvalidOrderIdException;
import chapter7.snippet.order.OrderIdInvalid;
import chapter7.snippet.order.OrderIdValid;
import chapter7.snippet.order.OrderIdValidationResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Supplementary snippets for Chapter 7.
 *
 * <p>This class collects a series of short, focused examples illustrating both
 * good and bad practices in error handling and logging.</p>
 */
@Slf4j
public class Snippets {
  private static final Logger logger = LoggerFactory.getLogger(Snippets.class);

  /**
   * Demonstrates proper use of a tight try-with-resources block for reading a file.
   *
   * <p>This method follows best practices by limiting the scope of the try block
   * to only the lines that may throw an exception and by handling exceptions
   * with specific log messages and unique error codes.</p>
   */
  public void readFileSafely() {
    try (BufferedReader reader = new BufferedReader(
      new FileReader("data.txt"))) {
      String line = reader.readLine();
      processLine(line);
    } catch (FileNotFoundException e) {
      log.warn("DATA_01: File 'data.txt' not found.", e);
    } catch (IOException e) {
      log.error("DATA_02: Failed to read from 'data.txt'.", e);
    }
  }

  private void processLine(String line) {
    if (line == null || line.isBlank()) {
      log.info("Skipping empty or blank line.");
      return;
    }
    log.info("Processing line: {}", line.trim());
  }

  /**
   * Demonstrates bad practice in error handling and logging.
   *
   * <p>This example prints stack traces and messages directly to standard output
   * and standard error, which is unsuitable for production code. Proper logging
   * frameworks like SLF4J should always be used instead.</p>
   *
   * @param data the input string expected to contain a numeric value
   */
  public void badConsoleLoggingExample(String data) {
    try {
      int parsedNumber = Integer.parseInt(data);
    } catch (NumberFormatException exception) {
      exception.printStackTrace();
      System.out.println("Big problem!");
      System.err.println("Big problem!");
    }
  }

  /**
   * Demonstrates a dangerous logging mistake where the log statement itself
   * causes a new exception.
   *
   * <p>The call to {@code data.toLowerCase()} inside the log message triggers a
   * {@link NullPointerException}. This pattern is an example of how poor logging
   * practices can create new failures during error handling.
   *
   * @return an integer result, always {@code 0} in this example
   */
  public void badLoggingCausesException() {
    String data = null;
    try {
      int parsedNumber = Integer.parseInt(data);
    } catch (NumberFormatException exception) {
      logger.error("Error while converting number: {} ", data.toLowerCase(),
        exception);
    }
  }

  /**
   * Demonstrates the worst possible error-handling practice—completely
   * ignoring exceptions.
   *
   * <p>The catch block is empty, meaning failures go undetected and the
   * system continues in an unknown state.</p>
   *
   * @param data the input string expected to contain a numeric value
   */
  public void swallowingExceptionSilently(String data) {
    try {
      int dataToTransform = Integer.parseInt(data);
    } catch (NumberFormatException exception) {
    }
  }

  /**
   * Demonstrates proper exception wrapping by converting a technical exception
   * into a domain-specific one.
   *
   * <p>This method converts {@link NumberFormatException} into a custom
   * {@link DataProcessingException}, providing a clear message
   * and context while preserving the original cause.</p>
   *
   * @param data the input string to be parsed
   * @return the parsed integer
   * @throws DataProcessingException if the input cannot be parsed
   */
  public int wrapTechnicalException(String data)
    throws DataProcessingException {
    int dataToTransform = 0;
    try {
      dataToTransform = Integer.parseInt(data);
    } catch (NumberFormatException exception) {
      throw new DataProcessingException(data, "Processing error", exception);
    }
    return dataToTransform;
  }

  /**
   * Demonstrates poor practice in exception design by reusing inappropriate
   * exception types.
   *
   * <p>This example throws generic or misleading exceptions that obscure the
   * true cause of failure, making debugging difficult.</p>
   *
   * @param data input data to validate
   * @param orderId the order identifier to check
   * @throws Exception incorrectly used generic exception for missing data
   * @throws NumberFormatException misleading exception for invalid order format
   */
  public void throwingMisleadingException(String data, String orderId)
    throws Exception {
    if (data == null || data.length() == 0) {
      throw new Exception();
    }
    if (!orderId.matches("\\d{8}")) {
      throw new NumberFormatException("Order ID must be exactly 8 digits.");
    }
  }

  /**
   * Demonstrates proper use of a domain-specific custom exception.
   *
   * <p>Throws {@link InvalidOrderIdException} when the
   * {@code orderId} does not conform to the expected 8-digit format.</p>
   *
   * @param orderId the order identifier to validate
   * @throws InvalidOrderIdException if the order ID is invalid
   */
  public void validateOrderIdOrThrow(String orderId)
    throws InvalidOrderIdException {
    if (!orderId.matches("\\d{8}")) {
      throw new InvalidOrderIdException(orderId,
        "Order ID must be exactly 8 digits.");
    }
  }

  /**
   * Part 1 - Validates the format of an order ID using a sealed class hierarchy
   * to represent results.
   *
   * <p>Returns either an {@link OrderIdValid} or
   * {@link OrderIdInvalid} instance depending on the input.</p>
   *
   * @param orderId the order identifier to validate
   * @return a validation result representing valid or invalid state
   */
  public OrderIdValidationResult checkOrderIdValidity(String orderId) {
    if (!orderId.matches("\\d{8}")) {
      return new OrderIdInvalid(orderId,
        "Order ID must be exactly 8 digits.");
    }
    return new OrderIdValid(orderId);
  }

  /**
   * Part 2 - Validates the format of an order ID using a sealed class hierarchy
   * to represent results.
   */
  public void handleOrderValidationResult() {
    switch (checkOrderIdValidity("orderId")) {
      case OrderIdValid(var id) -> processOrder(id);
      case OrderIdInvalid(_, var reason) -> alertOrderIssue(reason);
    }
  }

  private void processOrder(String s) {
    //mock
  }

  private void alertOrderIssue(String reason) {
    //mock
  }
}
