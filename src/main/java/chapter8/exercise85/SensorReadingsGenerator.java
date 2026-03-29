package chapter8.exercise85;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Exercise 11.5 - Making a sensor summary generator greener
 *
 * <p>This class generates a textual summary of sensor readings and is used to
 * illustrate several inefficiencies discussed in the memory and efficient
 * execution patterns. The code intentionally includes suboptimal practices.
 *
 * <p>The purpose of the exercise is to identify these sources of overhead and
 * refactor the implementation into a more memory-efficient and CPU-efficient
 * version using the principles of Chapter 8.</p>
 */
@Slf4j
public class SensorReadingsGenerator {
  public record Reading(String unit, int value, LocalTime timestamp) {}

  // deviceIds[i] is the ID of device i
  private final String[] deviceIds;

  // deviceReadings[i] is the array of readings for device i
  private final Reading[][] deviceReadings;

  public SensorReadingsGenerator(
    String[] deviceIds, Reading[][] deviceReadings) {
    this.deviceIds = deviceIds;
    this.deviceReadings = deviceReadings;
  }

  public String generateSummary() {

    List<String> summary = new ArrayList<>();

    // Using Stream<Integer> instead of IntStream → boxing overhead
    Stream<Integer> deviceIndexStream =
      Stream.iterate(0, i -> i + 1).limit(deviceIds.length);

    deviceIndexStream.forEach(i -> {
      List<String> tempList = new LinkedList<>();
      String deviceId = deviceIds[i];
      Reading[] readings = deviceReadings[i];

      // Nested loop building many strings via concatenation
      for (int j = 0; j < readings.length; j++) {
        Reading r = readings[j];
        String line =
          "Device:" + deviceId +
            " time=" + r.timestamp() +
            " value=" + r.value() +
            " unit=" + r.unit();
        tempList.add(line);
      }
      summary.add(tempList.toString() + "\n");
    });
    String result = summary.toString();
    return result;

  }

  public static void main(String[] args) {

    // Define three devices
    String[] deviceIds = {
      "sensor-A",
      "sensor-B"
    };

    // Create readings for each device (2D array)
    Reading[][] readings = new Reading[3][];

    // Device A readings
    readings[0] = new Reading[]{
      new Reading("°C", 22, LocalTime.of(10, 0)),
      new Reading("°C", 23, LocalTime.of(10, 5)),
      new Reading("°C", 21, LocalTime.of(10, 10))
    };

    // Device B readings
    readings[1] = new Reading[]{
      new Reading("%", 60, LocalTime.of(11, 20)),
      new Reading("%", 62, LocalTime.of(11, 25))
    };

    SensorReadingsGenerator generator = new SensorReadingsGenerator(deviceIds,
      readings);
    String summary = generator.generateSummary();

    log.info(summary);
  }

}
