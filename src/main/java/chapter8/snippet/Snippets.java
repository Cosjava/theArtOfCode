package chapter8.snippet;

import chapter8.mock.ExchangeRateService;
import chapter8.mock.WeatherService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Supplementary snippets for Chapter 8.
 * Snippets used to illustrate different approaches
 * to running concurrent tasks, including basic thread-pool execution,
 * try-with-resources management of executors, and retrieving results from
 * virtual-thread tasks using {@link Future}.
 */
@Slf4j
public class Snippets {

  /**
   * Launches tasks with a fixed thread pool.
   *
   * <p>This method demonstrates the simplest use of an {@link ExecutorService}
   * to run two fire-and-forget tasks concurrently. Each task triggers a
   * remote download operation, and the executor is explicitly shut down
   * once both tasks have been submitted.
   * The main thread does not wait for task completion.</p>
   */
  public void runWithFixedThreadPool() {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.execute(() -> new WeatherService()
      .download());
    executor.execute(() -> new ExchangeRateService()
      .download());
    executor.shutdown();
    log.info("Main thread finished starting tasks.");
  }

  /**
   * Modern thread pool management with try-with-resources.
   *
   * <p>This version wraps the executor in a try-with-resources block,
   * ensuring that the executor is automatically shut down when the method
   * completes. This improves robustness compared to the previous snippet,
   * where shutdown must be called manually.</p>
   */
  public void runWithAutoClosingExecutor() {
    try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
      executor.execute(() -> new WeatherService()
        .download());
      executor.execute(() -> new ExchangeRateService()
        .download());
      log.info("Main thread finished starting tasks.");
    }
  }

  /**
   * Retrieves results from virtual-thread tasks using Futures.
   */
  public void loadData() {
    try (ExecutorService executor =
           Executors.newVirtualThreadPerTaskExecutor()) {

      Future<Integer> weatherFuture = executor.submit(()
        -> new WeatherService().download());
      Future<Integer> exchangeFuture = executor.submit(()
        -> new ExchangeRateService().download());

      log.info("Main thread continues while tasks run...");

      handleResult("Weather records", weatherFuture);
      handleResult("Exchange records", exchangeFuture);
    }
  }

  /**
   * Handles retrieval and logging of a {@link Future} result.
   *
   * @param label  human-readable label describing the task
   * @param future the Future whose result should be retrieved and logged
   */
  private static void handleResult(String label, Future<Integer> future) {
    try {
      log.info(label + ": " + future.get());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Treatment interrupted " + label.toLowerCase(), e);
    } catch(ExecutionException e){
      log.error("Treatment failed " + label.toLowerCase(), e);
    }
  }
}
