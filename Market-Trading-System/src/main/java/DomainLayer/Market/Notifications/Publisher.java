package DomainLayer.Market.Notifications;

import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.IUserFacade;
import ServiceLayer.Market.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("Publisher")
public class Publisher {
    private final ApplicationEventPublisher eventPublisher;
    private Publisher instance;

    @Autowired
    private Publisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(Object source, String eventMessage, String userName) {
        Event event = new Event(source, eventMessage, new HashSet<>(Arrays.asList(userName)));
        eventPublisher.publishEvent(event);
    }
    public void publish(Object source, String eventMessage, List<String> userNames ) {
        Event event = new Event(source, eventMessage, new HashSet<>(userNames));
        eventPublisher.publishEvent(event);
    }
}
