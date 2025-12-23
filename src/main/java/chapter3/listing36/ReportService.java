package chapter3.listing36;

import chapter3.domain.Data;
import chapter3.listing36.generator.PdfReportGenerator;
import chapter3.listing36.generator.ReportGenerator;

import java.util.Collections;
import java.util.List;

/**
 * Listing 3.6 – Hiding complexity behind interfaces.
 *
 * <p>This listing completes the refactoring sequence that began with <strong>listing 3.4</strong>.
 * After reducing cognitive complexity in <strong>listing 3.5</strong> using a {@code switch}
 * expression, this version introduces an abstraction layer through interfaces.
 * Each report type is now encapsulated in its own implementation of
 * {@link chapter3.listing36.generator.ReportGenerator}, removing all branching
 * logic from the {@link ReportService} itself.</p>
 *
 * <p>The {@link #generateReport(chapter3.listing36.generator.ReportGenerator, java.util.List)}
 * method delegates report creation to a provided generator, such as
 * {@link chapter3.listing36.generator.PdfReportGenerator}.</p>
 */
public class ReportService {

  /**
   * Generates a report using the specified {@link ReportGenerator}.
   * This method delegates the actual generation to
   * the provided implementation.
   *
   * Example usage:
   * {@snippet :
   * //@highlight substring="PdfReportGenerator" type="bold":
   *   ReportGenerator generator = new PdfReportGenerator();
   *   List<Data> salesData = fetchSalesData();
   *   generateReport(generator, salesData);
   * }
   *
   * @param report the report generator to use
   * @param data the data to include in the report
   */
  public void generateReport(ReportGenerator report, List<Data> data) {
    report.generate(data);
  }

  public static void main(String[] args) {
    Data salesIncreaseDecember = new Data(20, "Sales increase for december");
    ReportService reportService = new ReportService();
    reportService.generateReport(new PdfReportGenerator(),
      Collections.singletonList(salesIncreaseDecember));
  }
}
