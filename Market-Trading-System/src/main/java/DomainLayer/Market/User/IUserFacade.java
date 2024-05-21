package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.UserPermission;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IUserFacade {
    public String createGuestSession();
    public boolean login(String userName, String password) throws Exception;
    public void terminateGuestSession(String userName);
    public void register(String userName, String password) throws Exception;
    public void logout(String userName);
    public String viewShoppingCart(String userName);
    public void modifyShoppingCart(String userName);
    public void checkoutShoppingCart(String userName);
    public boolean checkPermission(String userName);
    public void assignStoreOwner(String userName, long storeId, String newOwnerName, List<String> storePermissions);
    public void assignStoreManager(String userName, long storeId, String newManagerName, List<String> storePermissions);
    public List<StorePermission> getUserPermission(String userName);
    public void terminateGuest(int guestID);
    public void addItemToBasket(String userName, long itemId, long quantity);
    public void changeUserPermission(String userName, int permission);
}