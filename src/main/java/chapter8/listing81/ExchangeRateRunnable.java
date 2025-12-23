package chapter8.listing81;

import chapter8.mock.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 8.1 – Implementing Runnable for weather and exchange rate downloads.
 *
 * <p>This listing demonstrates the basic structure of a {@link Runnable}
 * implementation used in Chapter 8 to illustrate how tasks execute inside
 * threads. The class delegates the actual work to
 * {@link ExchangeRateService#download()}, then logs the result and
 * the thread name that performed the task.</p>
 */
@Slf4j
public class ExchangeRateRunnable implements Runnable {
  @Override
  public void run() {
    int nbRecords = new ExchangeRateService().download();
    log.info("Exchange rate records {}", nbRecords);
    log.info(Thread.currentThread().getName() + " finished!");
  }
}

