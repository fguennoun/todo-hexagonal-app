package rebelsrescue.fleet;

import ddd.AggregateRoot;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate Root representing a fleet of starships assembled for a rescue mission.
 * Encapsulates business rules and invariants about fleet composition.
 */
@AggregateRoot
public record Fleet(UUID id, List<StarShip> starships) {

    public Fleet(List<StarShip> starships) {
        this(UUID.randomUUID(), starships);
    }

    public Fleet {
        if (id == null) {
            throw new IllegalArgumentException("Fleet id cannot be null");
        }
        if (starships == null || starships.isEmpty()) {
            throw new IllegalArgumentException("Fleet must contain at least one starship");
        }
        // Make the list immutable
        starships = Collections.unmodifiableList(starships);
    }

    /**
     * Calculate total passenger capacity of this fleet.
     */
    public int totalPassengerCapacity() {
        return StarShip.totalPassengerCapacity(starships);
    }

    /**
     * Get the number of starships in this fleet.
     */
    public int size() {
        return starships.size();
    }

    /**
     * Check if this fleet can accommodate the requested number of passengers.
     */
    public boolean canAccommodate(int passengers) {
        return totalPassengerCapacity() >= passengers;
    }
}
