package chapter4.listing42;

import chapter4.domain.Book;
import chapter4.domain.BookFormat;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 4.2 – Refactoring with an enhanced switch expression.
 *
 * <p>This listing refactors the traditional {@code switch} statement from
 * <strong>listing 4.1</strong> into an <em>enhanced switch expression</em>. The new
 * structure eliminates the risk of fall-through, avoids unnecessary variable
 * declarations, and makes each branch’s outcome explicit through the
 * {@code yield} keyword.</p>
 *
 * <p>The {@link #getPromotionalMessage(Book)} method now returns the result
 * of the switch expression directly.</p>
 *
 */
@Slf4j
public class PromotionalBookService {

  public String getPromotionalMessage(Book book) {
    return switch (book.getFormat()) {
      case DIGITAL:
        yield "No trees were harmed!";
      case HARDCOVER:
        yield "Old-fashioned book.";
      case POCKET:
        String message = "This book compiles."
          + "\nYour brain might not.";
        yield message;
    };
  }

  public static void main(String[] args) {
    var service = new PromotionalBookService();

    var digital = new Book("Clean Code", BookFormat.DIGITAL);
    var pocket = new Book("The Pragmatic Programmer", BookFormat.POCKET);
    var hardcover = new Book("Design Patterns", BookFormat.HARDCOVER);

    log.info("DIGITAL:\n" + service.getPromotionalMessage(digital) + "\n");
    log.info("POCKET:\n" + service.getPromotionalMessage(pocket) + "\n");
    log.info("HARDCOVER:\n" + service.getPromotionalMessage(hardcover) + "\n");
  }

}
