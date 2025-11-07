package rebelsrescue.fleet.events.stubs;

import ddd.Stub;
import rebelsrescue.fleet.events.DomainEventPublisher;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub implementation of DomainEventPublisher for testing.
 * Captures published events for verification in tests.
 */
@Stub
public class InMemoryEventPublisher implements DomainEventPublisher {

    private final List<Object> publishedEvents = new ArrayList<>();

    @Override
    public void publish(Object event) {
        publishedEvents.add(event);
    }

    public List<Object> getPublishedEvents() {
        return new ArrayList<>(publishedEvents);
    }

    public <T> List<T> getEventsOfType(Class<T> eventType) {
        return publishedEvents.stream()
            .filter(eventType::isInstance)
            .map(eventType::cast)
            .toList();
    }

    public void clear() {
        publishedEvents.clear();
    }

    public int eventCount() {
        return publishedEvents.size();
    }
}

