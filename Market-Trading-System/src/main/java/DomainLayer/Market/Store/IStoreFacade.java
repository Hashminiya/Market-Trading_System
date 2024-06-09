package DomainLayer.Market.Store;


import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IStoreFacade {
    public static IStoreFacade getInstance(IRepository<Long, Store> storesRepo, IUserFacade userFacadeInstance, IPurchaseFacade purchaseFacadeInstance)
    {
        return StoreController.getInstance(storesRepo, userFacadeInstance, purchaseFacadeInstance);
    }
    public void setUserFacade(IUserFacade userFacade);
    public void setPurchaseFacade(IPurchaseFacade purchaseFacadeInstance);
    public long createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Discount> discounts)throws Exception;
    public HashMap<Long, Integer> viewInventoryByStoreOwner(String userId, long storeId) throws Exception;
    public long addItemToStore(String userId, long storeId, String itemName, double itemPrice, int stockAmount, String description, List<String> categories)throws Exception;
    public void updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int stockAmount)throws Exception;
    public void deleteItem(String userId, long storeId, long itemId)throws Exception;
    public void changeStorePolicy(String userId, long storeId);
    public void changeDiscountType(String userId, long storeId, String newType);
    public void assignStoreOwner(String userId, long storeId, String newOwnerId)throws Exception;
    public void assignStoreManager(String userId, long storeId, String newManagerId, List<String> permissions)throws Exception;
    public void removeStore(String userId, long storeId)throws Exception;
    public HashMap<String, List<String>> viewStoreManagementInfo(String userId, long storeId)throws Exception;
    public HashMap<Long, HashMap<Long, Integer>> viewPurchaseHistory(String userId, long storeId)throws Exception;
    public HashMap<Long, String> getAllProductsInfoByStore(long storeId);
    public HashMap<Long, String> getAllStoreInfo();
    public HashMap<Long,String> searchInStoreByCategory(long storeId, String category);
    public HashMap<Long,String> searchInStoreByKeyWord(long storeId, String keyWord);
    public HashMap<Long,String> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord);
    public HashMap<Long,String> searchGenerallyByCategory(String category);
    public HashMap<Long,String> searchGenerallyByKeyWord(String keyWord);
    public HashMap<Long,String> searchGenerallyByKeyWordAndCategory(String category, String keyWord);
    public boolean addItemToShoppingBasket(ShoppingBasket basket, long storeId, long itemId, int quantity);
    public void purchaseOccurs();
    public void calculateBasketPrice(ShoppingBasket basket, String code)throws Exception;
    public void addHiddenDiscount(double percent, Date expirationDate, List<Long> items, long storeId, String code, boolean isStoreDiscount);
    public void addRegularDiscount(double percent, Date expirationDate, List<Long> items, long storeId, List<Long> conditionItems, boolean isStoreDiscount);

    void clear();

    void setStoersRepo(IRepository<Long,Store> storesRepo);
}
