package chapter3.listing35;

import chapter3.domain.Data;
import chapter3.domain.ReportFormat;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Listing 3.5 – First refactoring: reducing cognitive complexity with a switch expression.
 *
 * <p>This listing refactors the conditional branching logic from <strong>listing 3.4</strong>,
 * replacing the long sequence of {@code if/else} statements with a modern
 * {@code switch} expression. The goal is to reduce cognitive complexity.</p>
 *
 * <p>The next step, shown in <strong>listing 3.6</strong>, will introduce
 * polymorphism by hiding the branching logic behind interfaces, further
 * improving extensibility and abstraction.</p>
 *
 */
@Slf4j
public class ReportService {

  public void generateReport(ReportFormat format, List<Data> data) {
    switch (format) {
      case PDF -> generatePdf(data);
      case CSV -> generateCsv(data);
      case EXCEL -> generateExcel(data);
      case null, default -> throw new UnsupportedOperationException(String.valueOf(format));
    }
  }

  private void generateExcel(List<Data> data) {
    //mock implementation
    log.info("Excel report generated successfully.");
  }

  private void generateCsv(List<Data> data) {
    //mock implementation
    log.info("Csv report generated successfully.");
  }

  private void generatePdf(List<Data> data) {
    //mock implementation
    log.info("Pdf report generated successfully.");
  }

  public static void main(String[] args) {
    Data salesIncreaseDecember = new Data(20, "Sales increase for december");
    chapter3.listing34.ReportService reportService = new chapter3.listing34.ReportService();
    reportService.generateReport(ReportFormat.CSV,
      Collections.singletonList(salesIncreaseDecember));
  }
}
