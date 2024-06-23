package DomainLayer.Market.User;

import java.util.Date;
import java.util.List;

import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserFacade {
    public static IUserFacade getInstance(UserRepository users, SystemManager systemManager, IStoreFacade storeFacade, IPurchaseFacade purchaseFacade)
    {
        return UserController.getInstance(users, systemManager, storeFacade, purchaseFacade);
    }
    public void setStoreFacade(IStoreFacade storeFacade);
    public void setPurchaseFacade(IPurchaseFacade purchaseFacade);
    public String createGuestSession();
    public boolean login(String userName, String password) throws Exception;
    public void terminateGuestSession(String userName);
    public void register(String userName, String password, int dateOfBirth) throws Exception;
    public void logout(String userName);
    public String viewShoppingCart(String userName) throws Exception;
    public void modifyShoppingCart(String userName, long storeId, long itemId, int newQuantity);
    public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) throws Exception;
    public boolean checkPermission(String userName,long storeId, String permission);
    public void assignStoreOwner(String userName, long storeId);
    public void assignStoreManager(String userName, long storeId, List<String> storePermissions);
    public List<String> getUserPermission(String userName,long storeId);
    public Long addItemToBasket(String userName,long storeId, long itemId, int quantity)throws Exception;
    public void addPermission(String userName, String userToPermit ,long storeId, String permission);
    public void removePermission(String userName, String userToUnPermit,long storeId, String permission);
    public boolean isRegister(String founderId);
    UserDetails loadUserByUsername(String userName);
    public boolean isAdmin(String userName);
    public int getUserAge(String userName);
    public void clear();
    public List<Long> viewUserStoresOwnership(String userName);
    public List<String> viewUserStoresNamesOwnership(String userName);

    ShoppingCart getShoppingCart(String userName);
}