package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;
import java.util.List;

import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.Util.IRepository;

public interface IUserFacade {
    public static IUserFacade getInstance(IRepository<String, User> users)
    {
        return UserController.getInstance(users);
    }
    public void setStoreFacade(IStoreFacade storeFacade);
    public String createGuestSession();
    public boolean login(String userName, String password) throws Exception;
    public void terminateGuestSession(String userName);
    public void register(String userName, String password, int dateOfBirth) throws Exception;
    public void logout(String userName);
    public String viewShoppingCart(String userName);
    public void modifyShoppingCart(String userName);
    public void checkoutShoppingCart(String userName);
    public boolean checkPermission(String userName,long storeId, String permission);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId, List<String> storePermissions);
    public List<String> getUserPermission(String userName,long storeId);
    public List<StorePermission> getUserPermission(String userName);
    public void terminateGuest(int guestID);
    public void addItemToBasket(String userName,long basketId, long itemId, int quantity);
    public void changeUserPermission(String userName, int permission);
    public boolean isRegister(String founderId);
}