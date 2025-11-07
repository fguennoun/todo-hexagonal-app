package rebelsrescue.fleet;

import ddd.ValueObject;

import java.math.BigDecimal;

/**
 * Value Object representing the number of passengers.
 * Encapsulates validation rules for passenger count.
 */
@ValueObject
public record PassengerCount(int value) {
    public PassengerCount {
        if (value <= 0) {
            throw new IllegalArgumentException("Passenger count must be positive, got: " + value);
        }
        if (value > 1_000_000) {
            throw new IllegalArgumentException("Passenger count too large: " + value);
        }
    }

    public boolean isGreaterThan(int other) {
        return value > other;
    }
}

