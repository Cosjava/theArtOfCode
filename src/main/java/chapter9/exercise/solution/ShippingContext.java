package chapter9.exercise.solution;

import chapter9.exercise.User;

import java.time.LocalDate;

public record ShippingContext(User user, ShippingType shippingType, LocalDate date) {}
