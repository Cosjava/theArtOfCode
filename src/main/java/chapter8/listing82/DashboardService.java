package chapter8.listing82;

import chapter8.listing81.ExchangeRateRunnable;
import chapter8.listing81.WeatherRunnable;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 8.2 – Creating and launching weather and exchange rate tasks.
 *
 * <p>This listing illustrates how two independent {@link Runnable} tasks
 * (a weather update task and an exchange-rate download task) are executed
 * concurrently using explicit {@link Thread} instances. Each thread is
 * given a descriptive name to improve observability in logs.</p>
 */
@Slf4j
public class DashboardService {
  public void loadData() {
    Thread weatherThread = new Thread(new WeatherRunnable(),
      "Weather task");
    Thread exchangeThread = new Thread(new ExchangeRateRunnable(),
      "Exchange rate task");

    weatherThread.start();
    exchangeThread.start();

    log.info("Main thread finished.");
  }

  public static void main(String[] args) {
    DashboardService dashboardService = new DashboardService();
    dashboardService.loadData();
  }
}
