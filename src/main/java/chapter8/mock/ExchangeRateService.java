package chapter8.mock;

import java.util.Random;

public class ExchangeRateService {
  public int download() {return new Random().nextInt();}
}
