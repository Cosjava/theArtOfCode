package chapter8.listing86;

import chapter8.mock.ExchangeRateService;
import chapter8.mock.WeatherService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Listing 8.6 – Beautiful chaining with virtual threads.
 *
 * <p>This listing refactors the virtual-thread workflow from the previous
 * example by extracting the key functional components into dedicated helper
 * methods. Supplying the tasks, combining results, handling failures, and
 * logging the final output are all expressed through named functions that
 * make the asynchronous pipeline clearer and more expressive.</p>
 *
 */
@Slf4j
public class DashboardService {
  public void loadData() {
    try (ExecutorService vExecutor = Executors
      .newVirtualThreadPerTaskExecutor()) {

      CompletableFuture<Integer> weatherFuture = CompletableFuture
        .supplyAsync(getWeatherSupplier(),
          vExecutor);

      CompletableFuture<Integer> exchangeFuture = CompletableFuture
        .supplyAsync(getExchangeSupplier(),
          vExecutor);

      weatherFuture
        .thenCombine(exchangeFuture,
          getCombineFunctionForWeatherAndExchange())
        .exceptionally(handleException())
        .thenAccept(logFinalTotal())
        .join();

    }
  }

  /**
   * Creates a supplier that triggers the weather download operation.
   *
   * <p>Used by {@code supplyAsync} to start the weather task on a virtual thread.
   * Returning a {@link java.util.function.Supplier} makes the asynchronous pipeline
   * easier to read and reuse.</p>
   *
   * @return a supplier that downloads weather data and returns the record count
   */
  private Supplier<Integer> getWeatherSupplier() {
    return () -> new WeatherService()
      .download();
  }

  /**
   * Creates a supplier that triggers the exchange-rate download operation.
   *
   * <p>This method parallels {@link #getWeatherSupplier()} and provides symmetry
   * in the asynchronous workflow. It encapsulates the logic needed to launch the
   * exchange task without exposing implementation details in the main pipeline.</p>
   *
   * @return a supplier that downloads exchange-rate data and returns the record count
   */
  private Supplier<Integer> getExchangeSupplier() {
    return () -> new ExchangeRateService()
      .download();
  }

  /**
   * Produces a combining function used to merge the results of the weather and
   * exchange-rate tasks.
   *
   * <p>The function logs both intermediate values before returning their sum.
   * Extracting this logic into a {@link java.util.function.BiFunction} clarifies
   * the behavior of the {@code thenCombine} stage.</p>
   *
   * @return a function that logs both results and returns their combined total
   */
  private BiFunction<Integer, Integer, Integer>
  getCombineFunctionForWeatherAndExchange() {
    return (weatherR, exchangeR) -> {
      log.info("Weather records: {}", weatherR);
      log.info("Exchange rate records: {}", exchangeR);
      return weatherR + exchangeR;
    };
  }

  /**
   * Provides a reusable exception handler for the asynchronous pipeline.
   *
   * <p>If any stage fails, this function logs the error and returns a fallback
   * value of zero to keep the pipeline alive. Centralizing exception handling
   * improves the readability of the main workflow.</p>
   *
   * @return a function that logs the exception and returns a fallback integer
   */
  private Function<Throwable, Integer> handleException() {
    return ex -> {
      log.error("Error during tasks execution", ex);
      return 0;
    };
  }

  /**
   * Creates a consumer that logs the final combined total.
   *
   * <p>This method makes the last stage of the CompletableFuture pipeline more
   * expressive by naming the action and isolating its logging behavior.</p>
   *
   * @return a consumer that logs the final aggregated total
   */
  private Consumer<Integer> logFinalTotal() {
    return total -> log.info("Final total returned: {}", total);
  }

  public static void main(String[] args) {
    DashboardService dashboardService = new DashboardService();
    dashboardService.loadData();
  }
}
