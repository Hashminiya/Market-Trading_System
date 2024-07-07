package API;
import DomainLayer.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHandler {

    @Value("${data.delete.on_shutdown:false}")
    private boolean deleteData;

    @EventListener(ContextClosedEvent.class)
    public void handleContextClosedEvent() {
        System.out.println("Application is shutting down!");

        if(deleteData) {
            deletePurchase();
            deleteItemDTO();
            deleteBasketItem();
            deleteItem();
            deleteBasket();
            deleteStore();
            deleteUser();
        }
    }

    private void deleteUser() {
        UserRepository users = SpringContext.getBean(DbUserRepository.class);
        System.out.println("Deleting all users...");
        users.deleteAll();
        System.out.println("All users deleted.\n");
    }

    private void deleteStore() {
        StoreRepository stores = SpringContext.getBean(DbStoreRepository.class);
        System.out.println("Deleting all stores...");
        stores.deleteAll();
        System.out.println("All stores deleted.\n");
    }

    private void deleteBasket() {
        BasketRepository baskets = SpringContext.getBean(DbBasketRepository.class);
        System.out.println("Deleting all baskets...");
        baskets.deleteAll();
        System.out.println("All baskets deleted.\n");
    }

    private void deleteItem() {
        ItemRepository items = SpringContext.getBean(DbItemRepository.class);
        System.out.println("Deleting all items...");
        items.deleteAll();
        System.out.println("All items deleted.\n");
    }

    private void deleteBasketItem() {
        BasketItemRepository basketItemRepository  = SpringContext.getBean(BasketItemRepository.class);
        System.out.println("Deleting all basketItems...");
        basketItemRepository.deleteAll();
        System.out.println("All basketItems deleted.\n");
    }

    private void deleteItemDTO() {
        ItemDTORepository itemDTORepository  = SpringContext.getBean(ItemDTORepository.class);
        System.out.println("Deleting all itemDTOs...");
        itemDTORepository.deleteAll();
        System.out.println("All itemDTOs deleted.\n");
    }

    private void deletePurchase() {
        PurchaseRepository purchaseRepository  = SpringContext.getBean(PurchaseRepository.class);
        System.out.println("Deleting all purchases...");
        purchaseRepository.deleteAll();
        System.out.println("All purchases deleted.\n");
    }
}