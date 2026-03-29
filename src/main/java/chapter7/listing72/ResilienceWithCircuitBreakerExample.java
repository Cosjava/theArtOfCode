package chapter7.listing72;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Listing 7.2 – Using a circuit breaker to protect against repeated failures.
 *
 * <p>This example demonstrates how to wrap an unreliable operation with a
 * Resilience4j {@link CircuitBreaker}. The circuit breaker monitors recent
 * calls, opens when failures exceed a configurable threshold, and prevents
 * the system from repeatedly invoking a failing dependency. This protects the
 * application from cascading failures and reduces unnecessary resource usage
 * .</p>
 */
@Slf4j
public class ResilienceWithCircuitBreakerExample {
  public static Supplier<String> getPaymentServiceWithCircuitBreaker() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
      .custom()
      .failureRateThreshold(50)
      .waitDurationInOpenState(Duration.ofMillis(5))
      .slidingWindowSize(5)
      .minimumNumberOfCalls(5)
      .build();

    CircuitBreaker circuitBreaker =
      CircuitBreakerRegistry
        .of(circuitBreakerConfig)
        .circuitBreaker("paymentService");

    return CircuitBreaker
      .decorateSupplier(circuitBreaker,
        getUnreliableService());
  }


  public static void main(String[] args) {
    Supplier<String> resilientCall =
      getPaymentServiceWithCircuitBreaker();

    IntStream
      .rangeClosed(1, 5000)
      .forEach(callNumber -> {
        try {
          String result = resilientCall.get();
          log.info("Call {}: {}", callNumber, result);
        } catch (Throwable e) {
          log.error("Call {}: failed ({})", callNumber, e.getMessage());
        }
      });
  }

  private static Supplier<String> getUnreliableService() {
    return () -> {
      if (Math.random() < 0.2) {
        throw new RuntimeException("Service failed!");
      }
      return "Success!";
    };
  }

}
