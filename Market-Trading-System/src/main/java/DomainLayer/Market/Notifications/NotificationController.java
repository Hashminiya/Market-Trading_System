package DomainLayer.Market.Notifications;

import DomainLayer.Market.Util.IdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("NotificationController")
public class NotificationController implements INotificationFacade {
    NotificationRepository notifications;
    public NotificationController(NotificationRepository notificationRepository){
        notifications = notificationRepository;
    }
    @Override
    public void save(String recipient, String content, Date date) {
        long notificationId = IdGenerator.generateId();
        Notification newNotification = new Notification(notificationId,recipient, content, date);
        notifications.save(newNotification);
    }

    @Override
    public List<String> getMessages(String userName) {
        List<Notification> userNotifications = notifications.findByRecipient(userName);
        return userNotifications.stream()
                .map(Notification::getContent)
                .collect(Collectors.toList());

    }
}
