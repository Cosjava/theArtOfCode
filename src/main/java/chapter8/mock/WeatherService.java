package chapter8.mock;

import java.util.Random;

public class WeatherService {
  public int download() {return new Random().nextInt();}
}
