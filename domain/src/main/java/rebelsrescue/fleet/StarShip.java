package rebelsrescue.fleet;

import ddd.ValueObject;

/**
 * Value Object representing a Star Wars starship.
 * Immutable and defined by its attributes.
 */
@ValueObject
public record StarShip(String name, int passengersCapacity, CargoCapacity cargoCapacity) {
    public StarShip {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("StarShip name cannot be blank");
        }
        if (passengersCapacity < 0) {
            throw new IllegalArgumentException("Passengers capacity cannot be negative");
        }
        if (cargoCapacity == null) {
            throw new IllegalArgumentException("Cargo capacity cannot be null");
        }
    }

    /**
     * Calculate total passenger capacity of multiple starships.
     */
    public static int totalPassengerCapacity(Iterable<StarShip> starShips) {
        int total = 0;
        for (StarShip starShip : starShips) {
            total += starShip.passengersCapacity;
        }
        return total;
    }
}
