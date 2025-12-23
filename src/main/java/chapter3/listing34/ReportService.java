package chapter3.listing34;

import chapter3.domain.Data;
import chapter3.domain.ReportFormat;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Listing 3.4 – A branching implementation for report generation.
 *
 * <p>This listing introduces another example for measuring and reducing
 * cognitive complexity. The {@link #generateReport(chapter3.domain.ReportFormat, java.util.List)}
 * method decides which report type to generate—PDF, CSV, or Excel—based on
 * a series of conditional branches.</p>
 *
 * <p>Each format is handled in a separate conditional
 * block, which makes the method harder to
 * extend (for example, when adding a new report type) and less open to reuse.</p>
 *
 * <p>The next refactoring in listing 3.5 will simplify this logic.</p>
 */
@Slf4j
public class ReportService {

  public void generateReport(ReportFormat format, List<Data> data) {
    if (ReportFormat.PDF.equals(format)) {
      generatePdf(data);
      return;
    } else if (ReportFormat.CSV.equals(format)) {
      generateCsv(data);
      return;
    } else if (ReportFormat.EXCEL.equals(format)) {
      generateExcel(data);
      return;
    }
    throw new UnsupportedOperationException(String.valueOf(format));
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
    ReportService reportService = new ReportService();
    reportService.generateReport(ReportFormat.CSV,
      Collections.singletonList(salesIncreaseDecember));
  }
}
