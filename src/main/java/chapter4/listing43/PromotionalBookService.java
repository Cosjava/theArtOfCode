package chapter4.listing43;

import chapter4.domain.Book;
import chapter4.domain.BookFormat;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 4.3 – Using arrow syntax for concise switch expressions.
 *
 * <p>This listing refines the enhanced switch expression from
 * <strong>listing 4.2</strong>
 * by adopting the arrow (→) syntax introduced in modern Java. This syntax
 * makes the {@code switch} both shorter and clearer, expressing each case
 * as a direct mapping from condition to result.</p>
 *
 * <p>The special case for {@code POCKET} uses a block to
 * define a small, local message before yielding the result—demonstrating
 * how this syntax can be put into a block if need be.</p>
 */
@Slf4j
public class PromotionalBookService {

  public String getPromotionalMessage(Book book) {
    return switch (book.getFormat()) {
      case DIGITAL -> "No trees were harmed!";
      case HARDCOVER -> "Old-fashioned book";
      case POCKET -> {
        String message = "This book compiles."
          + "\nYour brain might not.";
        yield message;
      }
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
