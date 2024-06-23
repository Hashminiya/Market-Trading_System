package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Component
@Scope("prototype")
public class User implements IUser,DataItem<String> {
    @Id
    private String userName;
    private String password;
    private int userAge;
    @Transient
    private Istate state;
    protected boolean loggedIn;
    @Transient
    private ShoppingCart shoppingCart;
    @Transient
    private Map<Long, Set<StoreEnum>> storePermissionsAndRole;

    public User(String userName, String password, int userAge, Istate state, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.state = state;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
        this.storePermissionsAndRole = new HashMap<>();
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserAge() {
        return userAge;
    }

    public Istate getUserState() {
        return state;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public String getId() {
        return userName;
    }

    @Override
    public String getName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserState(Istate state) {
        this.state = state;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public boolean login() {
        if (loggedIn) {
            throw new IllegalArgumentException("user already logged in");
        }
        loggedIn = true;
        return true;
    }

    public void logout() {
        verifyIsLoggedIn();
        loggedIn = false;
    }

    public void deleteShoppingBasket(long basketId) {
        verifyIsLoggedIn();
        shoppingCart.deleteShoppingBasket(basketId);
    }

    public void modifyShoppingCart(long basketId, long itemId, int newQuantity) {
        verifyIsLoggedIn();
        shoppingCart.modifyShoppingCart(basketId, itemId, newQuantity);
    }

    private void verifyIsLoggedIn() {
        if (!loggedIn) {
            throw new IllegalArgumentException("user not logged in");
        }
    }

    public List<String> getStorePermissions(long storeId) {
        List<String> permissions = new ArrayList<>();
        if (storePermissionsAndRole.containsKey(storeId)) {
            for (StoreEnum permission : storePermissionsAndRole.get(storeId)) {
                permissions.add(permission.name());
            }
        }
        return permissions;
    }

    public boolean checkPermission(long storeId, StorePermission storePermission) {
        if (storePermissionsAndRole.containsKey(storeId)) {
            if (storePermissionsAndRole.get(storeId).contains(storePermission)) {
                return true;
            }
            throw new IllegalArgumentException("user does not have " + storePermission.name() + " permission for this store");
        }
        throw new IllegalArgumentException("user does not have any permissions for this store");
    }

    public List<ItemDTO> checkoutShoppingCart(IStoreFacade storeFacade, String discountCode) throws Exception{
        verifyIsLoggedIn();
        return shoppingCart.checkoutShoppingCart(userName, storeFacade, discountCode);
    }

    public void assignStoreOwner(long storeId) {
        if(storePermissionsAndRole.containsKey(storeId) && storePermissionsAndRole.get(storeId).contains(StoreRole.OWNER)){
            throw new IllegalArgumentException("user is already store owner");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        permissions.add(StoreRole.OWNER); //Pay attention that if the user is already a manager, he will be both a manager and an owner!
        permissions.addAll(Arrays.asList(StorePermission.values()));
        storePermissionsAndRole.put(storeId, permissions);
    }

    public void assignStoreManager(long storeId, List<String> userPermissions) {
        if(storePermissionsAndRole.containsKey(storeId) && storePermissionsAndRole.get(storeId).contains(StoreRole.MANAGER)){
            throw new IllegalArgumentException("user is already store manager");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        permissions.add(StoreRole.MANAGER);
        for (String permission : userPermissions) {
            permissions.add(StorePermission.valueOf(permission));
        }
        storePermissionsAndRole.put(storeId, permissions);
    }

    public Long addItemToBasket(long storeId, long itemId, int quantity) {
        verifyIsLoggedIn();
        return shoppingCart.addItemBasket(storeId, itemId, quantity);
    }

    public boolean isRegister() {
        return state.isRegistered();
    }

    public void removePermission(long storeId, StorePermission storePermission) {
        storePermissionsAndRole.get(storeId).remove(storePermission);
    }

    public void addPermission(long storeId, StorePermission storePermission) {
        if (!storePermissionsAndRole.containsKey(storeId))
            storePermissionsAndRole.put(storeId, new HashSet<>());
        storePermissionsAndRole.get(storeId).add(storePermission);
    }
    public void clearShoppingCart(){
        this.shoppingCart.clear();
    }

    public List<Long> viewUserStoresOwnership() {
        List<Long> ownedStoreIds = new ArrayList<>();

        for (Map.Entry<Long, Set<StoreEnum>> entry : storePermissionsAndRole.entrySet()) {
            Long storeId = entry.getKey();
            Set<StoreEnum> roles = entry.getValue();

            // Check if the user is an OWNER of this store
            if (roles.contains(StoreRole.OWNER)) {
                ownedStoreIds.add(storeId);
            }
        }
        return ownedStoreIds;
    }
}
