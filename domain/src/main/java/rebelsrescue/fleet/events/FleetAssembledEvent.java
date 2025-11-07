package rebelsrescue.fleet.events;

import ddd.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event fired when a Fleet is successfully assembled.
 * This allows other parts of the system to react to fleet creation.
 */
@DomainEvent
public record FleetAssembledEvent(
    UUID eventId,
    UUID fleetId,
    int numberOfStarShips,
    int totalPassengerCapacity,
    int requestedPassengers,
    Instant occurredAt
) {
    public FleetAssembledEvent(UUID fleetId, int numberOfStarShips, int totalPassengerCapacity, int requestedPassengers) {
        this(UUID.randomUUID(), fleetId, numberOfStarShips, totalPassengerCapacity, requestedPassengers, Instant.now());
    }
}

