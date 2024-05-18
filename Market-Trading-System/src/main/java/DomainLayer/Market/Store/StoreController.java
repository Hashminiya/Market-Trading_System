package DomainLayer.Market.Store;

import java.util.HashMap;

public final class StoreController implements IStoreFacade{

    private static StoreController storeControllerInstance;

    private StoreController() {
        // private constructor to prevent instantiation
    }

    public static synchronized StoreController getInstance() {
        if (storeControllerInstance == null) {
            storeControllerInstance = new StoreController();
        }
        return storeControllerInstance;
    }

    @Override
    public void createStore(long founderId, String storeName, String storeDescription) {

    }

    @Override
    public void viewInventoryByStoreOwner(long userId, long storeId) {

    }

    @Override
    public void addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId) {

    }

    @Override
    public void updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int stockAmount) {

    }

    @Override
    public void deleteItem(long userId, long storeId, long itemId) {

    }

    @Override
    public void changeStorePolicy(long userId, long storeId) {

    }

    @Override
    public void changeDiscountType(long userId, long storeId, String newType) {

    }

    @Override
    public void assignStoreOwner(long actorId, long userId) {

    }

    @Override
    public void assignStoreManager(long actorId, long userId) {

    }

    @Override
    public void removeStore(long userId, long storeId) {

    }

    @Override
    public void viewStoreManagementInfo(long userId, long storeId) {

    }

    @Override
    public void viewPurchaseHistory(long userId, long storeId) {

    }

    @Override
    public HashMap<Long, HashMap<String, String>> getAllProductsInfoByStore(long storeId) {

        return null;
    }

    @Override
    public HashMap<Long, HashMap<String, String>> getAllStoreInfo(long storeId) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByName(String name) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByName(String name) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByCategory(long category) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByCategory(long category) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByKeyWord(String keyWord) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByKeyWord(String keyWord) {

        return null;
    }

    @Override
    public void addItemToShoppingBasket(long userId, long storeId, long itemId) {

    }
}
