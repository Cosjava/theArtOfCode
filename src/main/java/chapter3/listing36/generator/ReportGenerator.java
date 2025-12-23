package chapter3.listing36.generator;

import chapter3.domain.Data;

import java.util.List;

public interface ReportGenerator {
  void generate(List<Data> data);
}
