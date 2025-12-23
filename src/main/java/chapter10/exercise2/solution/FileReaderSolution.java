package chapter10.exercise2.solution;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Solution - Fix a security vulnerability
 *
 * <p>This class provides a secure alternative to the vulnerable file-reading
 * implementation shown in {@code FileReader}. It prevents path traversal
 * attacks by validating the filename and ensuring that the resolved file path
 * remains inside the intended base directory.</p>
 */
@Slf4j
public class FileReaderSolution {
  private static final String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\chapter10" +
    "\\exercise2\\";

  /**
   * Reads a file located under the configured base directory.
   *
   * @param filename the name of the file to read, relative to the base directory
   * @return the file's contents as a byte array
   * @throws IOException if the file cannot be opened or read
   * @throws SecurityException if the resolved file path is outside the base directory
   */
  public static byte[] readFile(String filename) throws IOException {
    Path basePath = Paths.get(BASE_PATH);
    log.info("Constructed base path {}", basePath);
    Path filePath = Paths.get(BASE_PATH + filename).toRealPath();
    log.info("Constructed file path {}", filePath);
    if (!filePath.startsWith(basePath)) {
      throw new SecurityException("Invalid file path");
    }
    return Files.readAllBytes(filePath);
  }

  /**
   * Validates the structure of a filename before it is used for file access.
   *
   * <p>This method enforces a strict whitelist of allowed characters
   * (letters, digits, dots, underscores, and dashes). Filenames that contain
   * characters outside this set are rejected. This check prevents attempts to
   * inject directory separators or other control characters that could lead to
   * unsafe file paths.</p>
   *
   * @param filename the filename to validate
   * @throws SecurityException if the filename contains disallowed characters
   */
  public static void validateFilename(String filename) throws IOException {
    Pattern patternToValidateFilename = Pattern.compile("^[a-zA-Z0-9._-]+$");
    if (!patternToValidateFilename.matcher(filename).matches()) {
      log.info("Invalid filename {}", filename);
      throw new SecurityException("Invalid characters in filename: " + filename);
    }
    log.info("Valid filename {}", filename);
  }

  public static void main(String[] args) throws IOException {
    var fileContent = readFile("text.txt");
    log.info("File content {}", new String(fileContent, StandardCharsets.UTF_8));
    var hostContent = readFile("..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\." + ".\\Windows" +
      "\\System32\\drivers\\etc\\hosts");
    log.info("File content {}", new String(hostContent, StandardCharsets.UTF_8));
  }

}
