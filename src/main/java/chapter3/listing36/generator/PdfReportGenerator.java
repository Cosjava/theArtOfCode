package chapter3.listing36.generator;

import chapter3.domain.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PdfReportGenerator implements ReportGenerator {

  @Override
  public void generate(List<Data> data) {
    //mock implementation
    log.info("PDF report generated successfully.");
  }
}
