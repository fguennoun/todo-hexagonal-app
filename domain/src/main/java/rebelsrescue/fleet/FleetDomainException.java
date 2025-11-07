package rebelsrescue.fleet;

/**
 * Base exception for all domain-related errors.
 * This follows DDD practice of having explicit domain exceptions.
 */
public class FleetDomainException extends RuntimeException {
    public FleetDomainException(String message) {
        super(message);
    }

    public FleetDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

