package chapter6.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
  private LocalDate signupDate;
  private boolean active;
  private String email;
}
