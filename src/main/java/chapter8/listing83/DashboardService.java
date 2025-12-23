package chapter8.listing83;

import chapter8.mock.ExchangeRateService;
import chapter8.mock.WeatherService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Listing 8.3 – Using Callable to return results from concurrent tasks.
 *
 * <p>This listing refactors the thread-handling logic from the earlier examples
 * by using a fixed thread pool and {@link Callable} tasks that return results.
 * It demonstrates how to submit tasks asynchronously, retrieve their outcomes
 * through {@link Future}, and handle failures or interruptions gracefully.</p>
 *
 */
@Slf4j
public class DashboardService {
  public void loadData() {
    ExecutorService executor = Executors.newFixedThreadPool(2);

    Callable<Integer> weatherTask = () -> new WeatherService().download();
    Callable<Integer> exchangeTask = () -> new ExchangeRateService().download();

    Future<Integer> weatherFuture = executor.submit(weatherTask);
    Future<Integer> exchangeFuture = executor.submit(exchangeTask);
    log.info("Main thread continues while tasks run...");

    try {
      int weatherCount = weatherFuture.get();
      log.info("Weather records: {}", weatherCount);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      exchangeFuture.cancel(true);
      log.error("Weather interrupted", e);
    } catch (ExecutionException e) {
      log.error("Weather failed", e);
    }

    try {
      int exchangeCount = exchangeFuture.get();
      log.info("Exchange rate records: {}", exchangeCount);
    } catch (CancellationException e) {
      log.warn("Exchange was cancelled because weather failed");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Exchange interrupted", e);
    } catch (ExecutionException e) {
      log.error("Exchange failed", e);
    }

    executor.shutdown();
    try {
      boolean terminationCompleted = executor.awaitTermination(10,
        TimeUnit.SECONDS);
      log.info("Termination completed {}", terminationCompleted);
    } catch (InterruptedException e) {
      log.error("Thread interrupted during shutdown", e);
      Thread.currentThread().interrupt();
    }
  }

  public static void main(String[] args) {
    DashboardService dashboardService = new DashboardService();
    dashboardService.loadData();
  }
}
