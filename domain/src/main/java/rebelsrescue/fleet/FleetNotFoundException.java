package rebelsrescue.fleet;

/**
 * Exception thrown when a Fleet is not found.
 */
public class FleetNotFoundException extends FleetDomainException {
    public FleetNotFoundException(String fleetId) {
        super(String.format("Fleet with id %s not found", fleetId));
    }
}

