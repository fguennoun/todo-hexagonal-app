package rebelsrescue.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rebelsrescue.fleet.events.FleetAssembledEvent;

/**
 * Example event listener that reacts to FleetAssembledEvent.
 * Demonstrates how other parts of the system can react to domain events.
 */
@Component
public class FleetAssembledEventListener {

    private static final Logger logger = LoggerFactory.getLogger(FleetAssembledEventListener.class);

    @EventListener
    public void onFleetAssembled(FleetAssembledEvent event) {
        logger.info("🚀 Fleet {} assembled with {} starships for {} passengers (capacity: {})",
            event.fleetId(),
            event.numberOfStarShips(),
            event.requestedPassengers(),
            event.totalPassengerCapacity());

        // Here you could:
        // - Send notifications
        // - Update analytics
        // - Trigger other business processes
        // - Send messages to a queue
    }
}

