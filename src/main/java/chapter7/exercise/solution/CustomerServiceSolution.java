package chapter7.exercise.solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Solution – Handling failure gracefully when loading customer data.
 *
 * <p>This class provides the refactored version of the exercise from
 * Chapter 7, <em>Handling failure with grace</em>. It demonstrates how to
 * correctly handle checked exceptions, apply a clean fallback strategy,
 * and log meaningful diagnostic messages.</p>
 *
 */
public class CustomerServiceSolution {
  private static final String BASE_FOLDER = System.getProperty(
    "user.dir") + "\\src\\main\\java\\chapter7" +
    "\\exercise\\data";
  private static final Path BASE_DIR = Path.of(BASE_FOLDER).normalize();

  private static final Logger logger =
    LoggerFactory.getLogger(CustomerServiceSolution.class);

  public Optional<String> loadCustomerData(String login)
    throws CustomerDataUnavailableException {
    String filename = convertToFilename(login);
    try {
      Path path = BASE_DIR.resolve(filename).normalize();
      if (!path.startsWith(BASE_DIR)) {
        logger.warn("Path traversal attempt blocked for login {}",
          hashForLogs(login));
        throw new CustomerDataUnavailableException(
          "Unable to read customer data");
      }
      return Optional.of(Files.readString(path));
    } catch (NoSuchFileException e) {
      return Optional.empty();
    } catch (InvalidPathException | IOException e) {
      logger.error("Failed to load customer data for login {} with error {}",
        hashForLogs(login), e.getClass().getSimpleName());
      throw new CustomerDataUnavailableException(
        "Unable to read customer data");
    }
  }

  private String convertToFilename(String login) {
    return login.toLowerCase() + ".json";
  }

  private String hashForLogs(String login) {
    return Integer.toHexString(
      Objects.requireNonNullElse(login, "").toLowerCase().hashCode()
    );
  }

  public static void main(String[] args)
    throws CustomerDataUnavailableException {
    CustomerServiceSolution service = new CustomerServiceSolution();
    String login = "JohnDoe";
    Optional<String> data = service.loadCustomerData(login);

    data.ifPresentOrElse(d -> logger.info("Customer Data:{}", d),
      () -> logger.info("Failed to load customer data for {}", login));

    String falseLogin = "unknownlogin";
    data = service.loadCustomerData(falseLogin);
    data.ifPresentOrElse(d -> logger.info("Customer Data:{}", d),
      () -> logger.info("Failed to load customer data for {}", falseLogin));
  }
}
