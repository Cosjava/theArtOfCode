package chapter8.listing85;

import chapter8.mock.ExchangeRateService;
import chapter8.mock.WeatherService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listing 8.5 – Beautiful chaining with virtual threads and CompletableFuture.
 *
 * <p>This listing demonstrates how {@link java.util.concurrent.CompletableFuture}
 * can be combined with a virtual-thread executor to run concurrent operations
 * without the overhead of platform threads. Both the weather download and
 * exchange-rate download execute asynchronously on lightweight virtual threads,
 * and their results are merged in a continuation pipeline.</p>
 */
@Slf4j
public class DashboardService {
  public void loadData() {
    try (ExecutorService vExecutor = Executors
      .newVirtualThreadPerTaskExecutor()) {

      CompletableFuture<Integer> weatherFuture = CompletableFuture
        .supplyAsync(() ->
            new WeatherService().download(),
          vExecutor);

      CompletableFuture<Integer> exchangeFuture = CompletableFuture
        .supplyAsync(() ->
            new ExchangeRateService().download(),
          vExecutor);

      weatherFuture.thenCombine(exchangeFuture,
          (weatherR, exchangeR) -> {
            log.info("Weather records: {}", weatherR);
            log.info("Exchange rate records: {}", exchangeR);
            return weatherR + exchangeR;
          }).exceptionally(ex -> {
          log.error("Error during tasks execution", ex);
          return 0;
        }).thenAccept(total
          -> log.info("Final total returned: " + total))
        .join();

    }
  }

  public static void main(String[] args) {
    DashboardService dashboardService = new DashboardService();
    dashboardService.loadData();
  }
}
