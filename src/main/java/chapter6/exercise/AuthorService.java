package chapter6.exercise;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

/**
 * Exercise – Refactoring imperative code into functional style using streams.
 *
 * <p>This exercise presents an imperative implementation that extracts a
 * {@link java.util.Map} associating each author with the set of their book
 * titles published within the past year. The task is to refactor this code
 * using the Java Stream API to achieve the same result in a more declarative
 * and expressive way.</p>
 *
 * <p><strong>Current logic:</strong></p>
 * <ul>
 *   <li>Iterates over a list of {@link Book} records.</li>
 *   <li>Checks whether each book’s {@code publishedDate} is within the last year.</li>
 *   <li>Groups valid books by author and collects their titles into a {@link java.util.Set}.</li>
 * </ul>
 *
 */
@Slf4j
public class AuthorService {

  public record Book(String author, String title, LocalDate publishedDate) {}

  public Map<String, Set<String>> getAuthorToRecentTitles(List<Book> books) {
    Map<String, Set<String>> authorToRecentTitles = new HashMap<>();
    for (Book book : books) {
      if (book
        .publishedDate()
        .isAfter(LocalDate
          .now()
          .minusYears(1))) {
        String author = book.author();
        String title = book.title();

        if (!authorToRecentTitles.containsKey(author)) {
          authorToRecentTitles.put(author, new HashSet<>());
        }
        authorToRecentTitles
          .get(author)
          .add(title);
      }
    }
    return authorToRecentTitles;
  }

  public static void main(String[] args) {
    var authorService = new AuthorService();
    Map<String, Set<String>> result = authorService.getAuthorToRecentTitles(getBooksForDemo());

    log.info("Authors with recent book titles (published within 1 year):");
    result.forEach((author, titles) -> {
      log.info(author + ": " + titles);
    });
  }

  private static List<Book> getBooksForDemo() {
    Book book1 = new Book("Alice Smith", "Modern Java", LocalDate
      .now()
      .minusMonths(3));
    Book book2 = new Book("Alice Smith", "Effective Streams", LocalDate
      .now()
      .minusYears(2));
    Book book3 = new Book("Bob Johnson", "Spring Boot Essentials", LocalDate
      .now()
      .minusMonths(6));
    Book book4 = new Book("Bob Johnson", "RESTful Services", LocalDate
      .now()
      .minusMonths(13));
    Book book5 = new Book("Charlie Ray", "Concurrency in Action", LocalDate
      .now()
      .minusDays(20));
    Book book6 = new Book("Charlie Ray", "Functional Java", LocalDate
      .now()
      .minusMonths(11));

    return List.of(book1, book2, book3, book4, book5, book6);
  }
}
