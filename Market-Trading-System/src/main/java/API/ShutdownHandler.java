package API;
import DomainLayer.Repositories.DbStoreRepository;
import DomainLayer.Repositories.DbUserRepository;
import DomainLayer.Repositories.StoreRepository;
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
        System.out.println("Application is shutting down!");
        System.out.println("Deleting all users...");
        users.deleteAll();
        System.out.println("All users deleted.\n");
        StoreRepository stores = SpringContext.getBean(DbStoreRepository.class);
        System.out.println("Deleting all stores...");
        stores.deleteAll();
        System.out.println("All stores deleted.\n");
    }
}