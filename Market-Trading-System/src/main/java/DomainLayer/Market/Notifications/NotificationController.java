package DomainLayer.Market.Notifications;

import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("NotificationController")
public class NotificationController implements INotificationFacade {
    private  static NotificationController instance;
    NotificationRepository notifications;
    @Autowired
    public NotificationController(NotificationRepository notificationRepository){
        notifications = notificationRepository;
    }
    public static synchronized NotificationController getInstance(NotificationRepository notifications){
        if(instance == null){
            instance = new NotificationController(notifications);
        }
        return instance;
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

    @Override
    public List<String> clear(String userName) {
        notifications.deleteAll();
        return new ArrayList<>();
    }
}
