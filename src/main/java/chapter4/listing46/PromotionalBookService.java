package chapter4.listing46;

import chapter4.listing45.Book;
import chapter4.listing45.DigitalBook;
import chapter4.listing45.HardcoverBook;
import chapter4.listing45.PocketBook;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Listing 4.6 – Using pattern matching with guards and null handling.
 *
 * <p>This listing refines the pattern-matching switch expression introduced
 * in <strong>listing 4.4</strong> by adding <em>guarded patterns</em> and explicit {@code null}
 * handling. It represents the most expressive and readable form of modern
 * Java control flow.</p>
 */
@Slf4j
public class PromotionalBookService {

  public String getPromotionalMessage(Book book) {

    return switch (book) {
      case DigitalBook digital ->
        "📚 Enjoy your digital copy of “" + digital.getTitle()
          + "”! Download it instantly at: " + digital.getDownloadLink();

      case PocketBook pocket when pocket.isColored() ->
        "🎨 The colorful pocket edition of “" + pocket.getTitle()
          + "” is now available — compact, vibrant, and perfect for travel reading!";

      case PocketBook pocket ->
        "📘 The classic pocket-sized “" + pocket.getTitle()
          + "” — lightweight, affordable, and easy to carry anywhere.";

      case HardcoverBook paper when paper.hasDustJacket() ->
        "📖 A premium hardcover edition of “" + paper.getTitle()
          + "” complete with a protective dust jacket — a true collector’s item!";

      case HardcoverBook paper ->
        "📗 The durable hardcover of “" + paper.getTitle()
          + "” — built for readers who love the feel of real pages.";

      case null ->
        "⚠️ No book information available — please provide a valid book.";

    };
  }

  public static void main(String[] args) {
    var service = new PromotionalBookService();

    Book digital = DigitalBook.builder().title("Clean Code")
      .downloadLink("https://books.example.com/cleancode").build();
    Book coloredPocket = PocketBook.builder().title("Effective Java").colored(true).build();
    Book plainPocket = PocketBook.builder().title("Refactoring").colored(false).build();
    Book hardcover = HardcoverBook.builder().title("Design Patterns").dustJacket(true).build();

    List<Book> books = List.of(digital, coloredPocket, plainPocket, hardcover);

    for (Book book : books) {
      log.info(service.getPromotionalMessage(book));
    }
  }
}
