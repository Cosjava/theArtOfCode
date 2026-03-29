package chapter8.snippet;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.LongStream;

/**
 * Demonstrates the batching technique from Chapter 8.
 *
 * <p>This snippet shows how to split a large workload into smaller batches
 * to smooth CPU usage and avoid overwhelming system resources.</p>
 */
@Slf4j
public class InvoiceGenerator {

  private static final int BATCH_SIZE = 200;

  /**
   * Splits the list of customer IDs into batches and generates invoices
   * for each batch. This simulates workload smoothing, a strategy that
   * reduces CPU spikes and improves energy efficiency.
   */
  public static void main(String[] args) {
    List<Long> customerIds = loadCustomerIds();
    List<List<Long>> batches = Lists.partition(customerIds, BATCH_SIZE);
    for (List<Long> batch : batches) {
      generateInvoices(batch);
    }
  }

  /**
   * Loads a mock list of customer IDs.
   *
   * <p>In a real application, this method would retrieve the data from a
   * database or an external service. Here it simulates a large dataset by
   * returning 10,000 sequential IDs.</p>
   *
   * @return a list of mock customer IDs
   */
  private static List<Long> loadCustomerIds() {
    //Mock data
    return LongStream.rangeClosed(1, 10_000)
      .boxed()
      .toList();
  }

  /**
   * Generates invoices for a single batch of customer IDs.
   *
   * <p>This method represents the heavy workload that benefits from
   * being split into smaller units. Logging is used to show batch size.</p>
   *
   * @param batch the customer IDs for which invoices must be generated
   */
  private static void generateInvoices(List<Long> batch) {
    log.info("Generating {} invoices...", batch.size());
    for (Long customerId : batch) {
      generatePdfInvoice(customerId);
    }
  }

  /**
   * Simulates the generation of a single PDF invoice.
   *
   * <p>In practice, this operation may involve expensive computation or
   * I/O (PDF creation, template processing, storage writes). Here we
   * mimic the cost with a short sleep call.</p>
   *
   * @param customerId the ID of the customer receiving the invoice
   */
  private static void generatePdfInvoice(Long customerId) {
    try {
      Thread.sleep(10); // Stand-in for PDF generation
    } catch (InterruptedException ignored) {}
  }
}
