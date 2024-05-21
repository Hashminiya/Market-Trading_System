package DomainLayer.Market.Store;


import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.User.IUserFacade;

import java.util.HashMap;
import java.util.List;

public interface IStoreFacade {
    public static IStoreFacade getInstance(IRepository<Long, Store> storesRepo, IPurchaseFacade purchaseFacade, IUserFacade userFacade)
    {
        return StoreController.getInstance(storesRepo, purchaseFacade, userFacade);
    }

    public void createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Item.Discount> discounts);
    public List<String> viewInventoryByStoreOwner(String userId, long storeId);
    public void addItemToStore(String userId, long storeId, String itemName, double itemPrice, int stockAmount, String description, List<String> categories);    public void updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int stockAmount);
    public void deleteItem(String userId, long storeId, long itemId);
    public void changeStorePolicy(String userId, long storeId);
    public void changeDiscountType(String userId, long storeId, String newType);
    public void assignStoreOwner(String userId, long storeId, String newOwnerId);
    public void assignStoreManager(String userId, long storeId, String newManagerId);
    public void removeStore(String userId, long storeId);
    public List<String> viewStoreManagementInfo(String userId, long storeId);
    public void viewPurchaseHistory(String userId, long storeId);
    public HashMap<Long, HashMap<String, String>> getAllProductsInfoByStore(long storeId);
    public HashMap<Long, HashMap<String, String>> getAllStoreInfo(long storeId);
    public HashMap<Long,String> searchInStoreByCategory(long storeId, String category);
    public HashMap<Long,String> searchInStoreByKeyWord(long storeId, String keyWord);
    public HashMap<Long,String> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord);
    public HashMap<Long,String> searchGenerallyByCategory(String category);
    public HashMap<Long,String> searchGenerallyByKeyWord(String keyWord);
    public HashMap<Long,String> searchGenerallyByKeyWordAndCategory(String category, String keyWord);
    public boolean addItemToShoppingBasket(ShoppingBasket basket, long storeId, long itemId, int quantity);
    public void purchaseOccurs();
}
