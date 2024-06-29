package DomainLayer.Market.Notifications;

import DomainLayer.Repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

public interface INotificationFacade {
    public static INotificationFacade getInstance(NotificationRepository notifications) {
        return NotificationController.getInstance(notifications);
    }

    public void save(String recipient, String content, Date date);
    public List<String> getMessages(String userName);

    List<String> clear(String userName);
}
