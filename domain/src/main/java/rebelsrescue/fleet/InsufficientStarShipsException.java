package rebelsrescue.fleet;

/**
 * Exception thrown when there are not enough starships to assemble a fleet.
 */
public class InsufficientStarShipsException extends FleetDomainException {
    public InsufficientStarShipsException(int requiredPassengers, int availableCapacity) {
        super(String.format("Cannot assemble fleet: need capacity for %d passengers but only %d available",
            requiredPassengers, availableCapacity));
    }
}

