package rebelsrescue.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import rebelsrescue.fleet.events.DomainEventPublisher;

/**
 * Spring-based implementation of DomainEventPublisher.
 * Adapts domain events to Spring's event system.
 */
@Component
@Primary
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher springEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher springEventPublisher) {
        this.springEventPublisher = springEventPublisher;
    }

    @Override
    public void publish(Object event) {
        springEventPublisher.publishEvent(event);
    }
}

