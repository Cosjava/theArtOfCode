package chapter10.listing105;

import chapter10.exception.PaymentException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.stream.IntStream;

/**
 * Listing 10.5 – Using a circuit breaker to protect against repeated failures.
 *
 * <p>This example demonstrates how to wrap an unreliable operation with a
 * Resilience4j {@link CircuitBreaker}. The circuit breaker monitors recent
 * calls, opens when failures exceed a configurable threshold, and prevents
 * the system from repeatedly invoking a failing dependency. This protects the
 * application from cascading failures and reduces unnecessary resource usage.</p>
 */
@Slf4j
public class ResilienceWithCircuitBreakerExample {
  private static CheckedSupplier<String> getPaymentServiceWithCircuitBreaker() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
      .custom()
      .failureRateThreshold(50)
      .waitDurationInOpenState(Duration.ofMillis(5))
      .slidingWindowSize(5)
      .build();

    CheckedSupplier<String> unreliableService = getUnreliableService();

    return Decorators
      .ofCheckedSupplier(unreliableService)
      .withCircuitBreaker(
        CircuitBreaker.of("paymentService", circuitBreakerConfig))
      .decorate();
  }

  public static void main(String[] args) {
    CheckedSupplier<String> resilientCall =
      getPaymentServiceWithCircuitBreaker();

    IntStream
      .rangeClosed(1, 5000)
      .forEach(callNumber -> {
        try {
          String result = resilientCall.get();
          log.info("Call {}: {}", callNumber, result);
        }
        catch (Throwable e) {
          log.error("Call {}: failed ({})", callNumber, e.getMessage());
        }
      });
  }

  private static CheckedSupplier<String> getUnreliableService() {
    return () -> {
      if (Math.random() < 0.8) {
        throw new PaymentException("Service failed!");
      }
      return "Success!";
    };
  }

}
