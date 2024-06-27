package API;
import DomainLayer.Repositories.*;
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
        ItemRepository items = SpringContext.getBean(DbItemRepository.class);
        System.out.println("Deleting all items...");
        items.deleteAll();
        System.out.println("All items deleted.\n");
        BasketRepository baskets = SpringContext.getBean(DbBasketRepository.class);
        System.out.println("Deleting all baskets...");
        baskets.deleteAll();
        System.out.println("All baskets deleted.\n");
    }
}