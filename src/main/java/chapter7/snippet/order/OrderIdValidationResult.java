package chapter7.snippet.order;

public sealed interface OrderIdValidationResult
  permits OrderIdValid, OrderIdInvalid {}

