package chapter8.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.LongStream;

/**
 * Exercise – Write a concurrent story.
 *
 * <p>The goal is to use this sequential version as a baseline, then
 * rewrite it with concurrency techniques from chapter 8 to observe
 * how performance changes.</p>
 */
@Slf4j
public class SumExercise {
  static long sum(int start, int end) {
    return LongStream.rangeClosed(start, end).sum();
  }

  public static void main(String[] args) {
    long start = System.nanoTime();
    long total = sum(1, 1_000_000);
    long end = System.nanoTime();
    log.info("Result {}, duration {}", total, end - start);
  }

}
