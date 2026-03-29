package chapter5.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
  private String title;
  private BookFormat format;
}
