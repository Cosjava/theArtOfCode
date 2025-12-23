package chapter7.exercise.solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Solution – Handling failure gracefully when loading customer data.
 *
 * <p>This class provides the refactored version of the exercise from
 * Chapter 7, <em>Handling failure with grace</em>. It demonstrates how to
 * correctly handle checked exceptions, apply a clean fallback strategy,
 * and log meaningful diagnostic messages.</p>
 *
 * <p>The {@link #loadCustomerData(String)} method reads a JSON file
 * containing customer data from the local file system. If the file cannot
 * be read (for example, it is missing, inaccessible, or corrupted),
 * the method logs an error with a clear, unique message and returns
 * an empty {@link java.util.Optional} instead of {@code null}.
 *
 * <p>This approach eliminates error-handling antipatterns such as
 * catching overly broad exceptions, printing stack traces directly to
 * standard output, or returning null values. It provides a resilient,
 * maintainable, and production-ready implementation.
 */
public class CustomerServiceSolution {
  private static final String BASE_PATH = System.getProperty(
    "user.dir") + "\\src\\main\\java\\chapter7" +
    "\\exercise\\data";

  private static final Logger logger =
    LoggerFactory.getLogger(CustomerServiceSolution.class);

  public Optional<String> loadCustomerData(String login) {
    String filename = convertToFilename(login);
    Path baseDir = Paths.get(BASE_PATH).toAbsolutePath().normalize();
    Path path = baseDir.resolve(filename).normalize();
    try {
      return Optional.of(Files.readString(path));
    } catch (IOException e) {
      logger.error("Failed to load customer data for filename {}",
        filename, e);
      return Optional.empty();
    }
  }

  private String convertToFilename(String login) {
    return login.toLowerCase() + ".json";
  }

  public static void main(String[] args) {
    CustomerServiceSolution service = new CustomerServiceSolution();
    String login = "JohnDoe";
    Optional<String> data = service.loadCustomerData(login);

    data.ifPresentOrElse(d -> logger.info("Customer Data:{}", d),
      () -> logger.info("Failed to load customer data for {}", login));
  }
}
