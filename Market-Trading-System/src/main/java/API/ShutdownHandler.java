package API;
import DomainLayer.Repositories.DbUserRepository;
import DomainLayer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHandler {


    @EventListener(ContextClosedEvent.class)
    public void handleContextClosedEvent() {
        UserRepository users = SpringContext.getBean(DbUserRepository.class);
        System.out.println("Application is shutting down. Deleting all users...");
        users.deleteAll();
        System.out.println("All users deleted.");
    }
}