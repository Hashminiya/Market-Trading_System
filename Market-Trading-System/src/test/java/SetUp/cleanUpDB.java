package SetUp;

import API.Utils.SpringContext;
import DomainLayer.Repositories.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class cleanUpDB {

    public static boolean clearDB(){
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("src/test/resources/application.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String activeProfile = props.getProperty("spring.profiles.active", "default");
        if ("db".equals(activeProfile)) {
            clearAllDB();
            return true;
        }
        return false;
    }
    public static void clearAllDB() {
        deletePurchase();
        deleteItemDTO();
        deleteBasketItem();
        deleteItem();
        deleteBasket();
        deleteStore();
        deleteUser();
    }

    private static void deleteUser() {
        UserRepository users = SpringContext.getBean(DbUserRepository.class);
        users.deleteAll();
    }

    private static void deleteStore() {
        StoreRepository stores = SpringContext.getBean(DbStoreRepository.class);
        stores.deleteAll();
    }

    private static void deleteBasket() {
        BasketRepository baskets = SpringContext.getBean(DbBasketRepository.class);
        baskets.deleteAll();
    }

    private static void deleteItem() {
        ItemRepository items = SpringContext.getBean(DbItemRepository.class);
        items.deleteAll();
    }

    private static void deleteBasketItem() {
        BasketItemRepository basketItemRepository  = SpringContext.getBean(BasketItemRepository.class);
        basketItemRepository.deleteAll();
    }

    private static void deleteItemDTO() {
        ItemDTORepository itemDTORepository  = SpringContext.getBean(ItemDTORepository.class);
        itemDTORepository.deleteAll();
    }

    private static void deletePurchase() {
        PurchaseRepository purchaseRepository  = SpringContext.getBean(PurchaseRepository.class);
        purchaseRepository.deleteAll();
    }
}
