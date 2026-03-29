package chapter8.exercise85.solution;

import chapter8.exercise85.SensorReadingsGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * Solution - Making a sensor summary generator greener
 *
 * <p>This class provides a memory-efficient and CPU-efficient rewrite of the
 * {@link chapter8.exercise85.SensorReadingsGenerator} implementation. It
 * removes the main sources of overhead found in the exercise.</p>
 */
@Slf4j
public class SensorReadingsGeneratorSolution {
  public record Reading(String unit, int value, LocalTime timestamp) {}

  private final String[] deviceIds;

  private final Reading[][] deviceReadings;

  public SensorReadingsGeneratorSolution(
    String[] deviceIds, Reading[][] deviceReadings) {
    this.deviceIds = deviceIds;
    this.deviceReadings = deviceReadings;
  }

  public String generateSummary() {
    StringBuilder summary = new StringBuilder();
    for (int i = 0; i < deviceIds.length; i++) {
      summary.append("Device ").append(deviceIds[i]).append(":\n");
      for (Reading r : deviceReadings[i]) {
        summary
          .append(" time=").append(r.timestamp())
          .append(" value=").append(r.value())
          .append(" unit=").append(r.unit())
          .append("\n");
      }
    }
    return summary.toString();
  }

  public static void main(String[] args) {

    // Define three devices
    String[] deviceIds = {
      "sensor-A",
      "sensor-B"
    };

    // Create readings for each device (2D array)
    SensorReadingsGenerator.Reading[][] readings =
      new SensorReadingsGenerator.Reading[3][];

    // Device A readings
    readings[0] = new SensorReadingsGenerator.Reading[]{
      new SensorReadingsGenerator.Reading("°C", 22, LocalTime.of(10, 0)),
      new SensorReadingsGenerator.Reading("°C", 23, LocalTime.of(10, 5)),
      new SensorReadingsGenerator.Reading("°C", 21, LocalTime.of(10, 10))
    };

    // Device B readings
    readings[1] = new SensorReadingsGenerator.Reading[]{
      new SensorReadingsGenerator.Reading("%", 60, LocalTime.of(11, 20)),
      new SensorReadingsGenerator.Reading("%", 62, LocalTime.of(11, 25))
    };

    SensorReadingsGenerator generator = new SensorReadingsGenerator(deviceIds,
      readings);
    String summary = generator.generateSummary();

    log.info(summary);
  }
}
