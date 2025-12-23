package chapter8.exercise.solution;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.stream.LongStream;

/**
 * Solution – Write a concurrent story.
 *
 * <p>This class provides a baseline sequential implementation and several
 * concurrent implementations of the range-sum computation introduced in the
 * exercise. Each method corresponds to a different concurrency mechanism
 * presented in chapter 8, allowing you to compare both performance and
 * programming style.</p>
 *
 * </ul>
 */
@Slf4j
public class SumSolution {
  static long sum(int start, int end) {
    return LongStream.rangeClosed(start, end).sum();
  }

  /**
   * Baseline sequential sum for comparison with concurrent versions.
   */
  public void runBaseline() {
    long start = System.nanoTime();
    long total = sum(1, 1_000_000);
    long end = System.nanoTime();
    log.info("Baseline result {}, duration {}", total, end - start);
  }

  /**
   * Concurrent solution – using Futures.
   *
   * <p>Splits the work into two tasks executed in a fixed thread pool and
   * combines their results. This demonstrates a minimal concurrent
   * decomposition using {@link ExecutorService} and {@link Future}.</p>
   */
  public void runWithFutures() {
    long start = System.nanoTime();
    try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

      Future<Long> f1 = executor.submit(() -> sum(1, 500_000));
      Future<Long> f2 = executor.submit(() -> sum(500_001, 1_000_000));

      long total = f1.get() + f2.get();
      long end = System.nanoTime();
      log.info("Futures result {}, duration {}", total, end - start);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Sum execution interrupted", e);
    } catch (ExecutionException e) {
      log.error("Error during the sum execution", e);
    }
  }

  /**
   * Concurrent solution – using CompletableFuture.
   *
   * <p>Runs the two halves asynchronously using {@link CompletableFuture} and
   * combines them through {@code thenCombine}. This version does not require
   * managing an executor explicitly.</p>
   */
  public void runWithCompletableFuture() {
    long start = System.nanoTime();

    CompletableFuture<Long> firstHalf =
      CompletableFuture.supplyAsync(() -> sum(1, 500_000));
    CompletableFuture<Long> secondHalf =
      CompletableFuture.supplyAsync(() -> sum(500_001, 1_000_000));

    long total = firstHalf.thenCombine(secondHalf, Long::sum).join();
    long end = System.nanoTime();

    log.info("CompletableFuture result {}, duration {}", total, end - start);
  }

  /**
   * Concurrent solution – parallel stream.
   *
   * <p>Relies on the parallel execution capabilities of {@code LongStream}.
   * The framework divides the work and schedules tasks automatically.</p>
   */
  public void runWithParallelStream() {
    long start = System.nanoTime();
    long total = LongStream.rangeClosed(1, 1_000_000)
      .parallel()
      .sum();
    long end = System.nanoTime();
    log.info("Parallel stream result {}, duration {}", total, end - start);
  }

  /**
   * Executes all variants of the exercise solution to allow direct comparison.
   */
  public static void main(String[] args) {
    SumSolution sum = new SumSolution();
    sum.runBaseline();
    sum.runWithFutures();
    sum.runWithCompletableFuture();
    sum.runWithParallelStream();
  }
}
