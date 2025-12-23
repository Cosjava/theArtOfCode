package chapter10.exercise2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exercise 10.2 - Fix a security vulnerability
 *
 * <p>This class demonstrates an unsafe file-reading implementation that builds
 * file paths by concatenating user-controlled input with a base directory.</p>
 */
@Slf4j
public class FileReader {
  private static final String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\chapter10" +
    "\\exercise2\\";

  public static byte[] readFile(String filename) throws IOException {
    Path path = Paths.get(BASE_PATH + filename).toRealPath();
    log.info("Constructed path {}", path);
    return Files.readAllBytes(path);
  }

  public static void main(String[] args) throws IOException {
    var fileContent = readFile("text.txt");
    log.info("File content {}", new String(fileContent, StandardCharsets.UTF_8));
    var hostContent = readFile("..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\." + ".\\Windows" +
      "\\System32\\drivers\\etc\\hosts");
    log.info("File content {}", new String(hostContent, StandardCharsets.UTF_8));
  }

}
