package DomainLayer.Market.Store;


import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Repositories.StoreRepository;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public interface IStoreFacade {
    public static IStoreFacade getInstance(StoreRepository storesRepo, IUserFacade userFacadeInstance, IPurchaseFacade purchaseFacadeInstance)
    {
        return StoreController.getInstance(storesRepo, userFacadeInstance, purchaseFacadeInstance);
    }
    public void setUserFacade(IUserFacade userFacade);
    public void setPurchaseFacade(IPurchaseFacade purchaseFacadeInstance);
    public long createStore(String founderId, String storeName, String storeDescription)throws Exception;
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
    public void purchaseOccurs(List<ShoppingBasket> baskets)throws InterruptedException;
    public void calculateBasketPrice(ShoppingBasket basket, String code)throws Exception;

    void clear();

    void setStoersRepo(StoreRepository storesRepo);
    public boolean checkValidBasket(ShoppingBasket shoppingBasket, String userName) throws InterruptedException;

    public void addDiscount(String userName, long storeId, String discountDetails) throws Exception;
    public void addPolicy(String userName, long storeId, String policyDetails) throws Exception;

    List<Store> findAll();

    Set<String> getAllCategories();
    //public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) throws Exception;
    public void restoreStock(List<ShoppingBasket> baskets)throws InterruptedException;

    public List<String> getListOfStorNamesByIds(List<Long> listOfIds);

    public List<Store> findStoresByOwner(String userName);
    Item getItem(Long key);

    String getStoreName(long storeId);
}
