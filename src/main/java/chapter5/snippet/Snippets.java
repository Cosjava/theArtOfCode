package chapter5.snippet;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Map;

/**
 * Supplementary snippets for Chapter 5.
 *
 * <p>This class gathers small, focused examples for section 5.2,
 * “The limits of expressiveness.” It contrasts three equivalent ways
 * of iterating over a map in Java—method reference, lambda expression,
 * and explicit loop—to show that shorter syntax is not always clearer
 * syntax, and that expressiveness must be balanced with readability.</p>
 */
@Slf4j
public class Snippets {

  /**
   * Adds all book entries to the catalog using a method reference.
   *
   * @param bookPublicationDates a map where the key is the book title and
   *                             the value
   *                             is its publication date
   */
  public void addBooksToCatalogWithMethodReference(
    Map<String, LocalDate> bookPublicationDates) {
    bookPublicationDates.forEach(this::addToCatalog);
  }

  /**
   * Adds all book entries to the catalog using a lambda expression.
   * @param bookPublicationDates a map where the key is the book title and the value
   *                             is its publication date
   */
  public void addBooksToCatalogWithLambda(
    Map<String, LocalDate> bookPublicationDates
  ) {
    bookPublicationDates.forEach((title, publicationDate) ->
      addToCatalog(title, publicationDate)
    );
  }

  /**
   * Adds all book entries to the catalog using an explicit for-each loop.
   * @param bookPublicationDates a map where the key is the book title and the value
   *                             is its publication date
   */
  public void addBooksToCatalogWithExplicitLoop(
    Map<String, LocalDate> bookPublicationDates
  ) {
    for (Map.Entry<String, LocalDate> entry : bookPublicationDates.entrySet()) {
      String title = entry.getKey();
      LocalDate publicationDate = entry.getValue();
      addToCatalog(title, publicationDate);
    }
  }

  private void addToCatalog(String title, LocalDate publicationDate) {
    // mock
  }
}
