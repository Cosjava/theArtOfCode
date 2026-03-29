package chapter5.listing52;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public final class PocketBook extends Book {
  private boolean colored;

}
