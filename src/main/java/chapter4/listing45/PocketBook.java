package chapter4.listing45;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public final class PocketBook extends Book {
  private boolean colored;

}
