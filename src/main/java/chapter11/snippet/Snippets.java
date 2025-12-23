package chapter11.snippet;

import chapter11.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Supplementary snippets for Chapter 11.
 *
 * <p>This class contains small, focused examples used to illustrate several
 * green coding practices discussed in the chapter 11.</p>
 */
public class Snippets {

  /**
   * Demonstrates inefficient string concatenation inside a loop,
   * which creates many temporary String objects and increases CPU
   * and memory usage.
   */
  public void inefficientConcatenation() {
    List<User> users = Collections.emptyList();
    String report = "";
    for (User user : users) {
      report += user.createProfile();
    }
  }

  /**
   * Demonstrates efficient string building using StringBuilder,
   * which reduces the number of unnecessary object allocations.
   */
  public void efficientConcatenation() {
    List<User> users = Collections.emptyList();
    StringBuilder reportBuilder = new StringBuilder();
    for (User user : users) {
      reportBuilder.append(user.createProfile());
    }
    String report = reportBuilder.toString();
  }

  /**
   * Demonstrates memory-efficient file processing using a stream.
   *
   * <p>This snippet reads a large file line by line without loading the entire
   * file into memory. This illustrates the memory efficiency pattern.</p>
   *
   * @throws IOException if the file cannot be opened or read
   */
  public void processFileStreaming() throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get("largefile.txt"))) {
      lines.forEach(line -> {
        // Process each line individually
      });
    }
  }

  /**
   * Demonstrates proper release of non-memory resources as part of the
   * memory efficiency pattern.
   *
   * @throws IOException if the file cannot be opened or read
   */
  public void readSafelyWithAutoClose() throws IOException {
    Path filePath = null; // Incorrect: uninitialized path
    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      process(reader.readLine());
    }
  }

  /**
   * Processes a line of text.
   *
   * <p>This is a placeholder method used in the snippet. In a real
   * implementation, it would perform domain-specific processing
   * on the line read from the file.</p>
   *
   * @param s the line of text to process
   */
  private void process(String s) {
    // mock
  }
}
