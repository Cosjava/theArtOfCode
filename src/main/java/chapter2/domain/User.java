package chapter2.domain;

public record User(String firstName, String lastName, String email, java.time.LocalDate birthDate,
                   String city) {}
