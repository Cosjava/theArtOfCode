package chapter6.snippet;

import chapter6.domain.User;
import chapter6.listing62.UserEmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Supplementary snippets for Chapter 6 – Streams, lambdas, and functional purity.
 *
 * <p>This class gathers all illustrative examples used throughout Chapter 6,
 * ranging from basic functions to advanced stream operations.</p>
 */
@Slf4j
public class Snippets {
  /**
   * Demonstrates an imperative approach to filtering and transforming a list.
   *
   * <p>This method processes a list of words using a traditional loop. It
   * filters words longer than three characters and adds their uppercase
   * versions to a result list. The algorithm is explicit and step-by-step,
   * showing exactly how the computation is performed.</p>
   *
   * @param words the list of input words
   * @return a new list containing uppercase versions of words longer than
   * three characters
   */
  public List<String> filterAndUppercaseImperative(List<String> words) {
    List<String> result = new ArrayList<>();
    for (String word : words) {
      if (word.length() > 3) {
        result.add(word.toUpperCase());
      }
    }
    return result;
  }

  /**
   * Demonstrates a functional approach to filtering and transforming a list.
   *
   * <p>This method performs the same logic as
   * {@link #filterAndUppercaseImperative(List)}, but uses a declarative,
   * stream-based style. It filters the words longer than three characters,
   * converts them to uppercase, and collects the results into a new list.</p>
   *
   * @param words the list of input words
   * @return a new list containing uppercase versions of words longer than
   * three characters
   */
  public List<String> filterAndUppercaseFunctional(List<String> words) {
    return words.stream()
      .filter(w -> w.length() > 3)
      .map(String::toUpperCase)
      .toList();
  }

  /**
   * Demonstrates the use of a predicate strategy for string validation.
   *
   * <p>This method illustrates how behavior can be represented as an object
   * implementing a functional interface. It creates a {@link StringPredicate}
   * instance—{@link LengthGreaterThanThree}—which defines a simple rule:
   * whether a string’s length is greater than three. The rule is then applied
   * by calling its {@code test()} method.</p>
   *
   * @param value the value to be tested
   */
  public void demonstratePredicateStrategy(String value) {
    StringPredicate myTestingFunction = new LengthGreaterThanThree();
    boolean isValid = myTestingFunction.test(value);
    log.info("Is '{}' valid (LengthGreaterThanThree strategy)? {}", value,
      isValid);
  }

  /**
   * Demonstrates how a lambda can be assigned to a variable and invoked.
   *
   * <p>This snippet defines a {@link StringPredicate} lambda that checks
   * if a string’s length is greater than three, assigns it to a variable,
   * and calls its {@code test()} method.</p>
   *
   * @param value the value to be tested
   */
  public void assignLambdaToVariable(String value) {
    StringPredicate myTestingFunction = x -> x.length() > 3;
    boolean isValid = myTestingFunction.test(value);
    log.info("Is '{}' valid (length > 3)? {}", value, isValid);
  }

  /**
   * Demonstrates how functions can be stored in a data structure.
   *
   * <p>This snippet creates a list of {@link StringPredicate} lambdas,
   * each representing a distinct condition. It retrieves and evaluates
   * the first function against a sample input.</p>
   *
   */
  public void storeLambdaInCollection() {
    List<StringPredicate> myFunctions = List.of(
      x -> x.length() > 3,
      x -> x.equals("Hello")
    );
    boolean isValid = myFunctions.getFirst().test("For the demo");
    log.info("Is 'For the demo' valid (first predicate)? {}", isValid);
  }

  /**
   * Returns a {@link StringPredicate} lambda that checks if a string’s
   * length is greater than three.
   *
   * <p>This demonstrates how a function can be returned from a method,
   * allowing it to be stored or applied later.</p>
   *
   * @return a {@link StringPredicate} representing a length check
   */
  public StringPredicate returnLambdaFromMethod() {
    return x -> x.length() > 3;
  }

  /**
   * Demonstrates how a function can be passed as a parameter and executed.
   *
   * <p>This method accepts a {@link StringPredicate} and a value, then
   * applies the predicate to the provided string. The example shows
   * a lambda being passed as a method argument.</p>
   *
   * @param function the predicate to test with
   * @param value    the input value
   * @return {@code true} if the predicate evaluates to true for the value,
   * otherwise {@code false}
   */
  public boolean testWithFunction(StringPredicate function, String value) {
    return function.test(value);
  }

  /**
   * Demonstrates passing a lambda as an argument to another method.
   *
   * <p>This snippet passes a {@link StringPredicate} lambda directly
   * to {@link #testWithFunction(StringPredicate, String)} and evaluates
   * the result.</p>
   */
  public void passLambdaAsParameter() {
    boolean isValid = testWithFunction(x -> x.length() > 3, "Hello");
    log.info("Is 'Hello' valid (length > 3)? {}", isValid);
  }

  /**
   * Demonstrates a basic {@link Function} that converts a {@link String}
   * into an {@link Integer} representing its length.
   *
   * <p>The input type is {@link String} and the output type is {@link Integer}.
   * The example calls {@link Function#apply(Object)} to compute the result.</p>
   */
  public void applyBasicFunction() {
    Function<String, Integer> wordCount = String::length;
    int result = wordCount.apply("How long am I?");
    log.info("String length: {}", result);
  }

  /**
   * Demonstrates function composition using {@link Function#compose(Function)}.
   *
   * <p>This example defines two functions: one that removes spaces from a
   * string
   * and another that counts the number of characters. The {@code compose()}
   * method
   * combines them so that {@code cleanSpaces} executes first, and its output is
   * passed to {@code wordCount}.</p>
   *
   * <p>The resulting function, {@code cleanAndCount}, can be reused wherever
   * this preprocessing and counting behavior is needed.</p>
   */
  public void composeFunctions() {
    Function<String, Integer> wordCount = String::length;
    Function<String, String> cleanSpaces = s -> s.replaceAll("\\s+", "");

    Function<String, Integer> cleanAndCount = wordCount.compose(cleanSpaces);
    int result = cleanAndCount.apply("How long am I?");
    log.info("Cleaned string length (no spaces): {}", result);
  }

  /**
   * Demonstrates function composition using {@link Function#andThen(Function)}.
   *
   * <p>This example defines one function to count the length of a string
   * and another to format that numeric result as a readable message.
   * The {@code andThen()} method ensures that {@code formatResult} executes
   * after the first function, taking its output as input.</p>
   *
   * <p>The final composed function returns a formatted message containing
   * the computed length.</p>
   */
  public void andThenFunctions() {
    Function<String, Integer> wordCount = String::length;
    Function<Integer, String> formatResult = result -> "Result: " + result;
    Function<String, String> countAndFormatResult =
      wordCount.andThen(formatResult);
    String result = countAndFormatResult.apply("How long am I?");
    log.info("Formatted function result: {}", result);
  }

  /**
   * Demonstrates how to use a {@link ToIntFunction} to compute
   * the length of a string as a primitive {@code int}.
   *
   * <p>This snippet defines a {@code ToIntFunction<String>} that returns
   * the number of characters in a given string. It then calls
   * {@link ToIntFunction#applyAsInt(Object)} to obtain the result.</p>
   *
   * <p>Using {@code ToIntFunction} avoids boxing overhead compared to
   * {@code Function<String, Integer>} and is more efficient for primitive
   * numeric results.</p>
   */
  public void computeStringLengthWithToIntFunction() {
    ToIntFunction<String> wordLength = s -> s.length();
    int result = wordLength.applyAsInt("How long am I?");
    log.info("Computed string length: {}", result);
  }

  /**
   * Demonstrates the most common ways to create a {@link Stream}.
   */
  public void streamCreation() {
    // From a collection (e.g., a List)
    Stream<String> streamFromList = List.of("a", "b", "c").stream();
    streamFromList.forEach(value -> log.info("From list: {}", value));

    // From an array
    String[] items = {"a", "b", "c"};
    Stream<String> streamFromArray = Arrays.stream(items);
    streamFromArray.forEach(value -> log.info("From array: {}", value));

    // Using Stream.of(...)
    // Convenient for directly creating a small stream from known elements
    Stream<String> streamOf = Stream.of("a", "b", "c");
    streamOf.forEach(value -> log.info("From stream: {}", value));

    // Using Stream.generate(...)
    // Creates an infinite stream, here generating random numbers
    Stream<Double> randoms = Stream.generate(Math::random);
    // Example of consuming the random stream (limited to 3 for demonstration)
    randoms.limit(3).forEach(value -> log.info("Random value: {}", value));
  }

  /**
   * Demonstrates two ways to create a parallel {@link Stream}.
   *
   * <p>Parallel streams divide data into multiple chunks
   * that can be processed concurrently. They can be obtained
   * either directly via {@link java.util.Collection#parallelStream()}
   * or by converting an existing sequential stream using
   * {@link Stream#parallel()}.</p>
   */
  public void createParallelStreams() {
    // Directly create a parallel stream from a collection
    Stream<String> parallelStreamDirect = List.of("a", "b", "c")
      .parallelStream();

    // Convert a sequential stream into a parallel one
    Stream<String> parallelStreamFromSequential =
      List.of("a", "b", "c").stream().parallel();

    // Example: consuming both (results order may vary)
    parallelStreamDirect.forEach(
      value -> log.info("Direct parallel stream value: {}", value));
    parallelStreamFromSequential.forEach(
      value -> log.info("Converted parallel stream value: {}", value));
  }

  /**
   * Demonstrates how to create and use primitive streams.
   *
   * <p>Primitive streams such as {@link IntStream} allow processing
   * numeric values without boxing overhead.</p>
   *
   * <p>This example includes:
   * <ul>
   *   <li>{@link IntStream#of(int...)} – creates a stream from given
   *   values</li>
   *   <li>{@link IntStream#range(int, int)} – generates a half-open range
   *   (end exclusive)</li>
   *   <li>{@link IntStream#rangeClosed(int, int)} – generates a closed range
   *   (end inclusive)</li>
   * </ul></p>
   */
  public void createPrimitiveStreams() {
    IntStream prices = IntStream.of(12, 2, 3, 47);
    prices.forEach(value -> log.info("Price: {}", value));

    IntStream range = IntStream.range(1, 5);
    range.forEach(value -> log.info("Range value: {}", value));

    IntStream closedRange = IntStream.rangeClosed(1, 5);
    closedRange.forEach(value -> log.info("Closed range value: {}", value));
  }

  /**
   * Demonstrates how to process a list of records using the Stream API.
   *
   * <p>This method defines a local {@code Book} record and showcases two
   * independent stream operations:
   * <ul>
   *   <li>Filtering books published within the last year and extracting
   *       their authors.</li>
   *   <li>Grouping all books by author to count how many titles each one
   *       has published.</li>
   * </ul>
   */
  public void processBooks() {
    record Book(String author, LocalDate publishedDate) {}
    List<Book> books = List.of(
      new Book("Alice Munro", LocalDate.now().minusMonths(6)),
      new Book("Alice Munro", LocalDate.now().minusYears(3)),
      new Book("Kazuo Ishiguro", LocalDate.now().minusMonths(10)),
      new Book("Toni Morrison", LocalDate.now().minusYears(2)),
      new Book("Toni Morrison", LocalDate.now().minusMonths(4))
    );

    // Filter books published within the last year and extract author names
    List<String> recentAuthors = books.stream()
      .filter(book -> book.publishedDate()
        .isAfter(LocalDate.now().minusYears(1)))
      .map(Book::author)
      .toList();
    log.info("Authors with recent publications: {}", recentAuthors);

    // Group books by author and count how many each has published
    Map<String, Long> booksPerAuthor = books.stream()
      .collect(Collectors.groupingBy(Book::author, Collectors.counting()));
    log.info("Books published per author: {}", booksPerAuthor);
  }

  /**
   * Demonstrates the use of a custom {@link Gatherer} to transform stream
   * elements.
   *
   * <p>This example uses the {@code Gatherer.ofSequential()} factory method
   * to create
   * a gatherer that duplicates each element: for every input {@code n}, it
   * emits
   * both {@code n} and {@code n * 2}. The resulting stream therefore produces
   * twice as many elements as the original one.</p>
   *
   * <p>The {@link Gatherer} API, introduced in Java 21, allows custom stateful
   * or stateless data transformations in streams while preserving parallel
   * and sequential processing capabilities.</p>
   */
  public void demonstrateGatherer() {
    Gatherer<Integer, ?, Integer> doubleGatherer = Gatherer.ofSequential(
      (state, element, downstream) -> {
        downstream.push(element);
        downstream.push(element * 2);
        return true;
      }
    );

    Stream.of(1, 2, 3, 4)
      .gather(doubleGatherer)
      .forEach(value -> log.info("Gathered value: {}", value));
  }

  record ReadResult(Optional<List<String>> result,
                    Optional<IOException> exception) {}

  /**
   * Demonstrates how to handle checked exceptions in a Stream pipeline.
   *
   * <p>This example reads file contents from a list of filenames and wraps
   * each result—either a successful read or an encountered exception—into
   * a {@code ReadResult} record. This approach allows stream processing to
   * continue even when individual file operations fail, preserving both
   * successes and errors for later analysis.</p>
   *
   * <p>It illustrates how {@link java.util.Optional} can be used to safely
   * represent the presence or absence of a value or exception, maintaining
   * a functional style without breaking the stream flow.</p>
   */
  public void handleExceptionsInStream() {
    List<String> filenames = List.of("filename1");
    List<ReadResult> contents = filenames.stream()
      .map(filename -> {
        try {
          List<String> content = Files.readAllLines(Path.of(filename));
          return new ReadResult(Optional.of(content), Optional.empty());
        } catch (IOException e) {
          return new ReadResult(Optional.empty(), Optional.of(e));
        }
      })
      .toList();

    contents.forEach(readResult -> {
      readResult.result().ifPresent(content ->
        log.info("File read successfully with {} lines", content.size()));
      readResult.exception().ifPresent(ex ->
        log.error("Error reading file: {}", ex.getMessage()));
    });
  }

  /**
   * Refactored version of {@link #handleExceptionsInStream()}.
   *
   * <p>This version extracts the file-reading logic into a helper method
   * {@link #readFileContent(String)}, simplifying the Stream pipeline.
   * Each {@link ReadResult} is created consistently and returned to the stream
   * collector for processing.</p>
   */
  public void handleExceptionsRefactored() {
    List<String> filenames = List.of("filename1");
    List<ReadResult> contents = filenames.stream()
      .map(this::readFileContent)
      .toList();

    contents.forEach(readResult -> {
      readResult.result().ifPresent(content ->
        log.info("File read successfully with {} lines", content.size()));
      readResult.exception().ifPresent(ex ->
        log.error("Error reading file: {}", ex.getMessage()));
    });
  }

  /**
   * Helper method that reads a file and returns a {@link ReadResult}.
   *
   * <p>This method encapsulates file I/O and exception handling logic,
   * returning a record that safely wraps both outcomes. It allows
   * {@link IOException}s to be handled gracefully without interrupting
   * the Stream pipeline.</p>
   *
   * @param filename the name of the file to read
   * @return a {@link ReadResult} containing either file contents or an
   * exception
   */
  private ReadResult readFileContent(String filename) {
    try {
      List<String> content = Files.readAllLines(Path.of(filename));
      return new ReadResult(Optional.of(content), Optional.empty());
    } catch (IOException e) {
      return new ReadResult(Optional.empty(), Optional.of(e));
    }
  }

  /**
   *
   * <p>This listing intentionally shows an "ugly" pipeline that, while
   * syntactically valid, is difficult to read and maintain. It chains
   * numerous intermediate operations making it hard to infer the intent
   * behind each transformation.</p>
   *
   * <p>The code collects recent active users, filters and transforms their
   * email addresses through several operations—case conversion, replacement,
   * sorting, and deduplication—and limits the output to the first 100 results.
   * Although the logic is correct, the readability is poor.</p>
   *
   * <p>This pipeline will be refactored in <strong>listing 6.2</strong> to
   * improve clarity.</p>
   */
  public void processUserEmails() {
    List<User> users = UserEmailService.getUsersWithEmail();
    List<String> recentSortedUppercaseEmails = users.stream()
      .filter(user -> user.isActive())
      .filter(user -> user.getSignupDate()
        .isAfter(LocalDate.now().minusYears(1)))
      .map(User::getEmail)
      .map(String::toLowerCase)
      .filter(email -> email.endsWith(".com"))
      .map(email -> email.replace("@", "[at]"))
      .sorted()
      .distinct()
      .limit(100)
      .toList();
    log.info("Processed {} email addresses: {}",
      recentSortedUppercaseEmails.size(), recentSortedUppercaseEmails);
  }

  /**
   * Demonstrates an illegal attempt to mutate an external variable from
   * inside a lambda.
   *
   * <p>In this example, the variable {@code count} is declared outside the
   * lambda
   * and incremented within the stream pipeline. The compiler rejects this
   * because variables used in lambdas must be <em>final</em> or
   * <em>effectively final</em>.
   * This restriction prevents side effects that would make the function
   * impure.</p>
   */
  public void mutateExternalVariableInLambda() {
    record Book(String author, LocalDate publishedDate) {}
    List<Book> books = Collections.emptyList();
    int count = 0; // Created outside the lambda

    // Compilation error: "Variable used in lambda expression should be final
    // or effectively final"
    List<String> authorNames = books.stream()
      .map(book -> {
        if (book.publishedDate().isAfter(LocalDate.now().minusYears(1))) {
          // Attempting to modify external state breaks functional purity
          // and is forbidden by the compiler.
          // count++; // Uncommenting this line causes compilation failure
        }
        return book.author();
      })
      .toList();

    log.info("Author names: {}", authorNames);
  }

  /**
   * Demonstrates how reassigning a captured variable breaks its effectively
   * final status.
   *
   * <p>If a variable used inside a lambda is reassigned afterward, it is no
   * longer
   * effectively final, and the compiler will forbid its use within the lambda.
   * This preserves referential transparency by preventing lambdas from
   * depending
   * on mutable external state.</p>
   */
  public void reassignEffectivelyFinalVariable() {
    record Book(String author, LocalDate publishedDate) {}
    List<Book> books = Collections.emptyList();

    String letter = "A";
    List<Book> authors = books.stream()
      .filter(book -> book.author().startsWith(letter))
      .toList();

    // Once reassigned, `letter` is no longer effectively final.
    // letter = "B"; // Uncommenting this line will cause compilation failure

    log.info("Authors starting with '{}': {}", letter, authors);
  }

  /**
   * Demonstrates how mutating an internal field of a final object breaks
   * purity.
   *
   * <p>Even when a reference is declared {@code final}, its internal state
   * can still
   * be modified if the class is mutable. This example shows how a field
   * mutation inside
   * a lambda—and even outside it—violates the principle of functional purity
   * .</p>
   *
   * <p>To preserve predictability and testability, prefer immutable types
   * such as records
   * for data used within functional pipelines.</p>
   */
  public void mutateFinalObjectState() {
    final Book myBook = new Book("The art of code", "SB", LocalDate.now());

    List<String> titles = Stream.of(myBook)
      .map(book -> {
        String title = myBook.getTitle().toUpperCase();
        myBook.setTitle(title); // Mutating internal state
        return title;
      })
      .toList();

    // The object can also be modified outside the pipeline
    myBook.setTitle("Hello");

    log.info("Transformed titles: {}", titles);
    log.info("Book after external mutation: {}", myBook);
  }

  @Data
  @AllArgsConstructor
  public static class Book {
    private String title;
    private String author;
    private LocalDate publishedDate;
  }
}
