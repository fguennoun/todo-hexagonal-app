package rebelsrescue.fleet;

import ddd.ValueObject;

import java.math.BigDecimal;

/**
 * Value Object representing cargo capacity.
 * Encapsulates validation and comparison logic.
 */
@ValueObject
public record CargoCapacity(BigDecimal value) {
    public CargoCapacity {
        if (value == null) {
            throw new IllegalArgumentException("Cargo capacity cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cargo capacity cannot be negative: " + value);
        }
    }

    public boolean isSufficientFor(CargoCapacity required) {
        return value.compareTo(required.value) >= 0;
    }

    public static CargoCapacity of(String value) {
        return new CargoCapacity(new BigDecimal(value));
    }

    public static CargoCapacity of(long value) {
        return new CargoCapacity(BigDecimal.valueOf(value));
    }
}

