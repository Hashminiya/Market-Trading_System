package DomainLayer.Market.User;

import java.util.Date;
import java.util.List;

import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;

public interface IUserFacade {
    public static IUserFacade getInstance(IRepository<String, User> users)
    {
        return UserController.getInstance(users);
    }
    public void setStoreFacade(IStoreFacade storeFacade);
    public void setPurchaseFacade(IPurchaseFacade purchaseFacade);
    public String createGuestSession();
    public boolean login(String userName, String password) throws Exception;
    public void terminateGuestSession(String userName);
    public void register(String userName, String password, int dateOfBirth) throws Exception;
    public void logout(String userName);
    public String viewShoppingCart(String userName);
    public void modifyShoppingCart(String userName, long storeId, long itemId, int newQuantity);
    public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) throws Exception;
    public boolean checkPermission(String userName,long storeId, String permission);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId, List<String> storePermissions);
    public List<String> getUserPermission(String userName,long storeId);
    public void addItemToBasket(String userName,long basketId, long itemId, int quantity);
    public void addPermission(String userName, long storeId, String permission);
    public void removePermission(String userName, long storeId, String permission);
    public boolean isRegister(String founderId);
}