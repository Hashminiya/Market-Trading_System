package DomainLayer.Market.User;

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
    public void createGuestSession();
    public boolean login(String userName, String password) throws Exception;
    public void terminateGuestSession(String userName);
    public void register(String userName, String password) throws Exception;
    public void logout(String userName);
    public String viewShoppingCart(String token);
    public void modifyShoppingCart(String token);
    public void checkoutShoppingCart(String token);
    public boolean checkPermission(String userName);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId);
    public List<Permission> getUserPermission(String userName);
}
