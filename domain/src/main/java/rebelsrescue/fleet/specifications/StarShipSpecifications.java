package rebelsrescue.fleet.specifications;

import rebelsrescue.fleet.CargoCapacity;
import rebelsrescue.fleet.StarShip;

import java.util.function.Predicate;

/**
 * Specification pattern for StarShip selection rules.
 * This encapsulates business rules in a reusable and testable way.
 */
public class StarShipSpecifications {

    public static final CargoCapacity MINIMAL_CARGO_CAPACITY = CargoCapacity.of(100_000L);

    /**
     * StarShip must have passenger capacity greater than zero.
     */
    public static Predicate<StarShip> hasPassengerCapacity() {
        return starShip -> starShip.passengersCapacity() > 0;
    }

    /**
     * StarShip must have sufficient cargo capacity for rescue missions.
     */
    public static Predicate<StarShip> hasSufficientCargoCapacity() {
        return starShip -> starShip.cargoCapacity().isSufficientFor(MINIMAL_CARGO_CAPACITY);
    }

    /**
     * StarShip is suitable for rescue missions (combines multiple criteria).
     */
    public static Predicate<StarShip> isSuitableForRescue() {
        return hasPassengerCapacity().and(hasSufficientCargoCapacity());
    }
}

