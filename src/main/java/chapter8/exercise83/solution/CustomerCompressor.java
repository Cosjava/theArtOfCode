package chapter8.exercise83.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Solution - Reducing the bandwidth usage of an endpoint
 *
 * <p>This class demonstrates three techniques for reducing payload size:
 * using MessagePack for binary serialization, shortening JSON field names,
 * and applying GZIP compression. It compares the size of each encoded
 * representation to highlight how data format and attribute naming impact
 * network and storage usage.</p>
 */
@Slf4j
public class CustomerCompressor {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {

    // ---- 1) Full version (id, name, ordersCount)
    List<CustomerReport> fullReports = List.of(
      new CustomerReport(48291, "Emily Doe", 3),
      new CustomerReport(12345, "John Smith", 5)
    );
    String fullReportsJson = mapper.writeValueAsString(fullReports);

    log.info("=== FULL ATTRIBUTE VERSION ===");
    byte[] msgpackFull = toMessagePackDtoFull(fullReports);
    byte[] gzipFull = toGzip(fullReportsJson.getBytes(StandardCharsets.UTF_8));
    logCompressionStats("FullReport", fullReportsJson, msgpackFull, gzipFull);

    // ---- 2) Short version (id, n, oc)
    List<CustomerReportShort> shortReports = List.of(
      new CustomerReportShort(48291, "Emily Doe", 3),
      new CustomerReportShort(12345, "John Smith", 5)
    );
    String shortReportsJson = mapper.writeValueAsString(shortReports);

    log.info("=== SHORT ATTRIBUTE VERSION ===");
    byte[] msgpackShort = toMessagePackDtoShort(fullReports);
    byte[] gzipShort = toGzip(
      shortReportsJson.getBytes(StandardCharsets.UTF_8));
    logCompressionStats("ShortReport", shortReportsJson, msgpackShort,
      gzipShort);

    log.info("=== MESSAGEPACK and GZIP VERSION ===");
    byte[] gzipMessagePackShort = toGzip(msgpackShort);
    log.info("Using messagePack and GZIP {}", gzipMessagePackShort.length);
  }

  /**
   * Encodes a list of full {@link CustomerReport} objects into MessagePack.
   *
   * <p>This version preserves the descriptive attribute names
   * {@code id}, {@code name}, and {@code ordersCount}, which results in a
   * larger payload compared to the short-field variant.</p>
   */
  static byte[] toMessagePackDtoFull(List<CustomerReport> list)
    throws IOException {
    try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
      packer.packArrayHeader(list.size());
      for (CustomerReport c : list) {
        packer.packMapHeader(3);
        packer.packString("id").packInt(c.id());
        packer.packString("name").packString(c.name());
        packer.packString("ordersCount").packInt(c.ordersCount());
      }
      return packer.toByteArray();
    }
  }

  /**
   * Encodes a list of {@link CustomerReport} objects into MessagePack using
   * shortened attribute names to minimize payload size.
   *
   * <p>This snippet demonstrates how renaming fields in DTOs reduces
   * network traffic by shrinking serialized output.</p>
   */
  static byte[] toMessagePackDtoShort(List<CustomerReport> list)
    throws IOException {
    try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
      packer.packArrayHeader(list.size());
      for (CustomerReport c : list) {
        packer.packMapHeader(3);
        packer.packString("id").packInt(c.id());
        packer.packString("n").packString(c.name());
        packer.packString("oc").packInt(c.ordersCount());
      }
      return packer.toByteArray();
    }
  }

  /**
   * Compresses the given byte array with GZIP.
   */
  static byte[] toGzip(byte[] input) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
      gzip.write(input);
    }
    return baos.toByteArray();
  }

  /**
   * Logs the size of JSON, MessagePack, and GZIP representations
   * of the same logical data.
   */
  private static void logCompressionStats(
    String label, String json, byte[] msgpack, byte[] gzip) {
    log.info("{} - JSON: {}", label, json);
    log.info("{} - JSON size: {} bytes", label, json.getBytes().length);
    log.info("{} - MessagePack size: {} bytes", label, msgpack.length);
    log.info("{} - GZIP(JSON) size: {} bytes", label, gzip.length);
  }

  /**
   * Full version of the customer report used in Exercise 11.3.
   *
   * <p>This record represents the verbose JSON structure, where each field
   * uses a descriptive name. It is used to illustrate how longer attribute
   * identifiers increase the size of payloads.</p>
   */
  public record CustomerReport(int id, String name, int ordersCount) {}

  /**
   * Compact version of the customer report used in Exercise 11.3.
   *
   * <p>This record uses shortened attribute names to reduce the size of the
   * output. It demonstrates how smaller identifiers help decrease
   * payload size for network transmission and storage.</p>
   */
  public record CustomerReportShort(int id, String n, int oc) {}
}
