package DomainLayer.Market.Notification;
import java.util.Set;
import org.springframework.context.ApplicationEvent;

public class Event  extends ApplicationEvent {
    private String message;
    private Set<String> userIds; // Set of user IDs who should receive the notification

    public Event(Object source, String message, Set<String> userIds) {
        super(source);
        this.message = message;
        this.userIds = userIds;
    }

    public String getMessage() {
        return message;
    }

    public Set<String> getUserIds() {
        return userIds;
    }
}
