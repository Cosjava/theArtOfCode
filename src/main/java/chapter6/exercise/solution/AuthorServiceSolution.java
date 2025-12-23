package chapter6.exercise.solution;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Solution – Refactoring the imperative grouping logic into a functional Stream pipeline.
 *
 * <p>This class provides a functional implementation of the exercise from
 * {@code chapter6.exercise.AuthorService}. It replaces manual iteration and
 * map population with a declarative, stream-based approach that achieves the
 * same result in a more compact and expressive way.</p>
 *
 * <p><strong>Functional logic:</strong></p>
 * <ul>
 *   <li>Creates a stream of {@link Book} records.</li>
 *   <li>Filters books to include only those published within the last year.</li>
 *   <li>Groups the remaining books by author using
 *       {@link java.util.stream.Collectors#groupingBy(java.util.function.Function, java.util.stream.Collector)}.</li>
 *   <li>Collects each author’s titles into a {@link java.util.Set} using
 *       {@link java.util.stream.Collectors#mapping(java.util.function.Function, java.util.stream.Collector)}.</li>
 * </ul>
 */
@Slf4j
public class AuthorServiceSolution {

  public record Book(String author, String title, LocalDate publishedDate) {}

  public Map<String, Set<String>> getAuthorToRecentTitles(
    List<Book> books) {
    return books
      .stream()
      .filter(book -> book
        .publishedDate()
        .isAfter(LocalDate
          .now()
          .minusYears(1)))
      .collect(Collectors.groupingBy(Book::author,
        Collectors.mapping(Book::title, Collectors.toSet())));
  }

  public static void main(String[] args) {
    var authorService = new AuthorServiceSolution();
    Map<String, Set<String>> result = authorService.getAuthorToRecentTitles(
      getBooksForDemo());

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


