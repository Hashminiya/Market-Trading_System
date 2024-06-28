package DomainLayer.Market.Notifications;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
@Scope("singleton")
public interface NotificationRepositoryDB extends NotificationRepository {
}
