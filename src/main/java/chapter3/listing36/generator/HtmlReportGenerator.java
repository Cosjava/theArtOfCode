package chapter3.listing36.generator;

import chapter3.domain.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class HtmlReportGenerator implements ReportGenerator {

  @Override
  public void generate(List<Data> data) {
    //mock implementation
    log.info("Html report generated successfully.");
  }
}
