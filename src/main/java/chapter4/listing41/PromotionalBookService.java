package chapter4.listing41;

import chapter4.domain.Book;
import chapter4.domain.BookFormat;
import chapter4.exception.UnsupportedBookFormatException;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 4.1 – A traditional switch statement with fall-through.
 *
 * <p>This listing demonstrates a traditional {@code switch} statement
 * that constructs promotional messages for books based on their format.
 * The code includes an intentional flaw: a missing {@code break}
 * statement in one of the cases, which causes an unintended fall-through.
 * This design illustrates how easily such oversights can happen.</p>
 */
@Slf4j
public class PromotionalBookService {

  public String getPromotionalMessage(Book book) throws UnsupportedBookFormatException {
    String message;
    switch (book.getFormat()) {
      case DIGITAL:
        message = book.getTitle() + "\nNo trees were harmed in the making of this eBook!";
        break;
      case POCKET:
        message = book.getTitle() + "\nThis book compiles. Your brain might not.";
        //default forgotten on purpose to demonstrate fall-through
      case HARDCOVER:
        message = "A hardcover with soft dependencies: " + book.getTitle();
        break;
      default:
        throw new UnsupportedBookFormatException(book.getFormat());
    }
    return message;
  }

  public static void main(String[] args) {
    var service = new PromotionalBookService();

    var digital = new Book("Clean Code", BookFormat.DIGITAL);
    var pocket = new Book("The Pragmatic Programmer", BookFormat.POCKET);
    var hardcover = new Book("Design Patterns", BookFormat.HARDCOVER);

    try {
      log.info("DIGITAL:\n" + service.getPromotionalMessage(digital) + "\n");
      log.info("POCKET:\n" + service.getPromotionalMessage(pocket) + "\n");
      log.info("HARDCOVER:\n" + service.getPromotionalMessage(hardcover) + "\n");
    } catch (UnsupportedBookFormatException e) {
      log.error(e.getMessage());
    }
  }

}
