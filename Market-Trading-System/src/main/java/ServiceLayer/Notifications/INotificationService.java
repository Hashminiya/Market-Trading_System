package ServiceLayer.Notifications;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface INotificationService {
    public void save(String recipient, String content, Date date);
    public ResponseEntity<?> getMessages(String token);
}
