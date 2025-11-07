package rebelsrescue.fleet.events;

/**
 * Port for publishing domain events.
 * Implementations might use Spring Events, messaging systems, etc.
 */
public interface DomainEventPublisher {
    void publish(Object event);
}

