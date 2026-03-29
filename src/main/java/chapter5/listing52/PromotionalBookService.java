package chapter5.listing52;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Listing 5.2 – Using type pattern matching in switch expressions.
 *
 * <p>This listing introduces <em>type pattern matching</em>. Instead of
 * switching on an enum or a field,
 * the {@link #getPromotionalMessage(Book)} method now switches
 * directly on the runtime type of the {@link Book} object.</p>
 *
 * <p>Each case pattern matches against a concrete subclass—
 * {@link DigitalBook}, {@link PocketBook},
 * and {@link HardcoverBook}—automatically casting it to the
 * appropriate type and binding it to a local variable.</p>
 */
@Slf4j
public class PromotionalBookService {

  public String getPromotionalMessage(Book book) {

    return switch (book) {
      case DigitalBook digital -> digital.getDownloadLink();
      case PocketBook _ -> "Same book, smaller format!";
      case HardcoverBook _ -> "Old-fashioned book…";
    };
  }

  public static void main(String[] args) {
    var service = new PromotionalBookService();

    Book digital = DigitalBook.builder().title("Clean Code")
      .downloadLink("https://books.example.com/cleancode").build();
    Book coloredPocket = PocketBook.builder().title("Effective Java")
      .colored(true).build();
    Book plainPocket = PocketBook.builder().title("Refactoring").colored(false)
      .build();
    Book hardcover = HardcoverBook.builder().title("Design Patterns")
      .dustJacket(true).build();

    List<Book> books = List.of(digital, coloredPocket, plainPocket, hardcover);

    for (Book book : books) {
      log.info(service.getPromotionalMessage(book));
    }
  }
}
