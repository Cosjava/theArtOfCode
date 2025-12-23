package chapter10.exercise1.solution;

import chapter10.exercise1.User;

import java.time.LocalDate;

public record ShippingContext(User user, ShippingType shippingType, LocalDate date) {}
