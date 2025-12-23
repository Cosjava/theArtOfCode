package chapter4.listing45;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public final class DigitalBook extends Book {
  private String downloadLink;
}
