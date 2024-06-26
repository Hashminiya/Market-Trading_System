package DomainLayer.Market.Notifications;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service("Publisher")
public class Publisher {
    private final ApplicationEventPublisher eventPublisher;

    public Publisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(Event event) {
        eventPublisher.publishEvent(event);
    }
}
