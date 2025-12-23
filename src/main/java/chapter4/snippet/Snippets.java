package chapter4.snippet;

import chapter4.domain.Book;
import chapter4.listing45.DigitalBook;
import chapter4.listing45.HardcoverBook;
import chapter4.listing45.PocketBook;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static chapter4.domain.BookFormat.DIGITAL;

/**
 * Supplementary snippets for Chapter 4.
 * <p>This class gathers small, focused examples used throughout Chapter 4 to
 * illustrate how recent Java language enhancements improve expressiveness.</p>
 */
@Slf4j
public class Snippets {

  /**
   * Updates book information based on its {@link chapter4.domain.BookFormat}.
   *
   * <p>This method demonstrates a modern {@code switch} statement that performs
   * different actions depending on the format of the provided {@link Book}.
   * It groups multiple cases—{@code HARDCOVER} and {@code POCKET}—into a single
   * branch, reducing redundancy and improving readability.</p>
   */
  public void updateBookDataByFormat(Book book) {
    switch (book.getFormat()) {
      case DIGITAL -> updateMetadata(book);
      case HARDCOVER, POCKET -> updateCatalog(book);
    }
  }

  private void updateCatalog(Book book) {
    //mock
  }

  private void updateMetadata(Book book) {
    //mock
  }

  /**
   * Demonstrates polymorphism through a common {@link Book} interface.
   *
   * <p>This method creates a list of different {@link Book} subtypes—
   * {@link DigitalBook}, {@link PocketBook}, and {@link HardcoverBook}—
   * each initialized with its own attributes (such as download link,
   * color printing, or dust jacket). Despite their differences, all
   * objects are stored in a single {@code List<Book>}.</p>
   */
  public void logBookPageCounts() {
    chapter4.listing45.Book digital = DigitalBook.builder().pageCount(500).title("Clean Code")
      .downloadLink("https://books.example.com/cleancode").build();
    chapter4.listing45.Book coloredPocket = PocketBook.builder().title("Effective Java")
      .pageCount(510).colored(true).build();
    chapter4.listing45.Book plainPocket = PocketBook.builder().title("Refactoring").pageCount(500)
      .colored(false).build();
    chapter4.listing45.Book hardcover = HardcoverBook.builder().title("Design Patterns")
      .pageCount(500).dustJacket(true).build();
    List<chapter4.listing45.Book> books = List.of(digital, coloredPocket, plainPocket, hardcover);

    for (chapter4.listing45.Book book : books) {
      log.info("Page count: " + book.getPageCount() + " — " + book.getTitle());
    }
  }

  /**
   * Demonstrates classic {@code instanceof} type checking before refactoring
   * to pattern matching.
   *
   * <p>This method checks whether the provided {@link Book} instance is a
   * {@link HardcoverBook} before performing a type-specific operation.
   * If the check succeeds, the object is explicitly cast and its
   * {@link HardcoverBook#printDustCover()} method is invoked.</p>
   *
   * <p>This approach illustrates the traditional use of {@code instanceof}
   * followed by a manual cast, which was necessary prior to Java’s
   * introduction of pattern matching for {@code instanceof}. While this
   * version is correct, it adds visual and cognitive noise by requiring
   * two statements to perform one conceptual action. In
   * {@link #printDustCoverModern(chapter4.listing45.Book)}, this logic will be
   * refactored into a modern version using <em>pattern matching</em>.</p>
   */
  public void printDustCoverLegacy(chapter4.listing45.Book bookToPrint) {
    if (bookToPrint instanceof HardcoverBook) {
      HardcoverBook hardcoverBook = (HardcoverBook) bookToPrint;
      hardcoverBook.printDustCover();
    }
  }

  /**
   * <p>This method refactors the previous
   * {@link #printDustCoverLegacy(chapter4.listing45.Book)} implementation to use
   * <em>pattern matching</em> for {@code instanceof}, a modern Java feature
   * that simplifies type checks and casts into a single, expressive
   * statement.</p>
   */
  public void printDustCoverModern(chapter4.listing45.Book bookToPrint) {
    if (bookToPrint instanceof HardcoverBook hardcoverBook) {
      hardcoverBook.printDustCover();
    }
  }
}
