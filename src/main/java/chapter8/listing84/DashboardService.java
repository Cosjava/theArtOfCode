package chapter8.listing84;

import chapter8.mock.ExchangeRateService;
import chapter8.mock.WeatherService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Listing 8.4 – Using CompletableFuture to run asynchronous tasks.
 *
 * <p>This listing rewrites the concurrent workflow using
 * {@link CompletableFuture}, eliminating explicit thread management and
 * simplifying the coordination of asynchronous results. Both the weather
 * download and exchange-rate download run in parallel, and their results
 * are combined once both tasks complete.</p>
 *
 */
@Slf4j
public class DashboardService {
  public void loadData() {
    CompletableFuture<Integer> weatherFuture =
      CompletableFuture.supplyAsync(()
        -> new WeatherService().download());

    CompletableFuture<Integer> exchangeFuture =
      CompletableFuture.supplyAsync(()
        -> new ExchangeRateService().download());

    log.info("Main thread continues while tasks run...");

    CompletableFuture<Integer> combined =
      weatherFuture
        .thenCombine(exchangeFuture,
          (weatherR, exchangeR) -> {
            log.info("Weather: {}", weatherR);
            log.info("Exchange: {}", exchangeR);
            return weatherR + exchangeR;
          });

    combined.exceptionally(ex -> {
      log.error("Error during tasks execution", ex);
      return 0;
    });

    int total = combined.join();
    log.info("Final total returned: {}", total);
  }

  public static void main(String[] args) {
    DashboardService dashboardService = new DashboardService();
    dashboardService.loadData();
  }
}
