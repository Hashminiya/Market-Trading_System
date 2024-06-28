package DomainLayer.Market.Notifications;

import java.util.Date;
import java.util.List;

public interface INotificationFacade {
    public void save(String recipient, String content, Date date);
    public List<String> getMessages(String userName);
}
