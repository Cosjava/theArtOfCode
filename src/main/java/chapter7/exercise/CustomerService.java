package chapter7.exercise;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Exercise – Handling failure gracefully when loading customer data.
 *
 * <p>This exercise accompanies Chapter 7, <em>Handling failure with grace</em>,
 * and focuses on identifying and correcting poor error-handling practices.</p>
 *
 * <p>The {@link #loadCustomerData(String)} method currently reads a JSON file
 * representing customer data from the local file system using
 * {@link java.nio.file.Files#readString(java.nio.file.Path)}.
 * The implementation contains several error-handling antipatterns that
 * should be identified and corrected.</p>
 *
 * <p>The goal of this exercise is to practice writing resilient, meaningful,
 * and maintainable failure-handling code.</p>
 */
public class CustomerService {
  private static final String BASE_PATH = System.getProperty(
    "user.dir") + "\\src\\main\\java\\chapter7" +
    "\\exercise\\data";

  public String loadCustomerData(String login) {
    Path path = null;
    try {
      String filename = convertToFilename(login);
      Path baseDir = Paths.get(BASE_PATH).toAbsolutePath().normalize();
      path = baseDir.resolve(filename).normalize();
      return Files.readString(path);
    } catch (Exception e) {
      System.out.println("Exception caught " + login.toLowerCase()
        + " " + path);
      e.printStackTrace();
      return null;
    }
  }

  private String convertToFilename(String login) {
    return login.toLowerCase() + ".json";
  }

  public static void main(String[] args) {
    CustomerService service = new CustomerService();
    String login = "JohnDoe";
    String data = service.loadCustomerData(login);

    if (data != null) {
      System.out.println("Customer Data:\n" + data);
    } else {
      System.out.println("Failed to load customer data for " + login);
    }
  }
}
