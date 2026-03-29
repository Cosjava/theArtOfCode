package chapter5.listing52;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
public final class HardcoverBook extends Book {
  private boolean dustJacket;

  public boolean hasDustJacket() {
    return dustJacket;
  }

  public void printDustCover() {
    //mock implementation
    log.info("Printing dust cover");
  }

}
