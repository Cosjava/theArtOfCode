package chapter4.listing44;

import chapter4.listing45.Book;
import chapter4.listing45.DigitalBook;
import chapter4.listing45.HardcoverBook;
import chapter4.listing45.PocketBook;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Listing 4.4 – Using type pattern matching in switch expressions.
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
      case DigitalBook digital ->
        "📚 Enjoy your digital copy of “" + digital.getTitle() + "”! " +
          "Download it instantly "
          + "at: " + digital.getDownloadLink();

      case PocketBook pocket -> pocket.isColored() ?
        "🎨 The pocket edition of “" + pocket.getTitle() + "” now includes " + "colorful " +
          "illustrations — perfect for on-the-go reading!" : "📘 The classic "
        + "pocket-sized "
        + "“" + pocket.getTitle() + "” — small, light, and easy to carry " +
        "anywhere.";

      case HardcoverBook paper ->
        paper.hasDustJacket() ?
          "📖 A collector’s edition hardcover of “" + paper.getTitle() +
          "”" + " complete with a protective dust jacket — built to last!" :
          "📗 A sturdy " +
          "hardcover " + "edition of “" + paper.getTitle() + "” for readers " +
            "who love the feel of "
          + "real pages.";

      default -> "Just buy it ! " + book.getTitle();
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
