package chapter5.listing52;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract sealed class Book
  permits DigitalBook, PocketBook, HardcoverBook {
  private String title;
  private int pageCount;
}
