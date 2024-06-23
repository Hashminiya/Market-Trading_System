package DomainLayer.Market.Notification;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

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
