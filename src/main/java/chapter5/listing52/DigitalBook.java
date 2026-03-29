package chapter5.listing52;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public final class DigitalBook extends Book {
  private String downloadLink;
}
