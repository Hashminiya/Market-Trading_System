package DomainLayer.Repositories;

import DomainLayer.Market.Notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
@NoRepositoryBean
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(String userName);
}
