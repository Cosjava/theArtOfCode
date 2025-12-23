package chapter1.domain;

import java.math.BigDecimal;

public record Price(Currency currency, BigDecimal amount) {}
