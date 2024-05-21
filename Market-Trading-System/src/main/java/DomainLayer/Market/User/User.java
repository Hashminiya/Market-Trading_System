package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;

import java.util.*;

public class User implements DataItem<String> {
    private String userName;
    private String password;
    private int userAge;
    private Istate state;
    private boolean loggedIn;
    private ShoppingCart shoppingCart;
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

    public boolean logout() {
        verifyIsLoggedIn();
        loggedIn = false;
        return true;
    }

    public boolean addToShoppingCart(int storeNum, int productNum, int quantity) {
        verifyIsLoggedIn();
        return shoppingCart.addToShoppingCart(storeNum, productNum, quantity);
    }

    public boolean deleteShoppingBasket(int storeNum, int productNum) {
        verifyIsLoggedIn();
        return shoppingCart.deleteShoppingBasket(storeNum, productNum);
    }

    public boolean modifyShoppingCart(long basketId, long itemId, int newQuantity) {
        verifyIsLoggedIn();
        return shoppingCart.modifyShoppingCart(basketId, itemId, newQuantity);
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
            throw new IllegalArgumentException("user does not have" + storePermission.name() + "permission for this store");
        }
        throw new IllegalArgumentException("user does not have any permissions for this store");
    }

    public List<ItemDTO> checkoutShoppingCart(IStoreFacade storeFacade, String discountCode) {
        verifyIsLoggedIn();
        return shoppingCart.checkoutShoppingCart(storeFacade, discountCode);
    }

    public void assignStoreOwner(long storeId, List<String> userPermissions) {
        if(userPermissions.contains(StoreRole.OWNER.name())){
            throw new IllegalArgumentException("user is already store owner");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        for (String permission : userPermissions) {
            permissions.add(StorePermission.valueOf(permission));
        }
        permissions.add(StoreRole.OWNER);
        storePermissionsAndRole.put(storeId, permissions);
    }

    public void assignStoreManager(long storeId, List<String> userPermissions) {
        if(userPermissions.contains(StoreRole.OWNER.name()) || userPermissions.contains(StoreRole.MANAGER.name())){
            throw new IllegalArgumentException("user is already store owner or manager");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        for (String permission : userPermissions) {
            permissions.add(StorePermission.valueOf(permission));
        }
        permissions.add(StoreRole.MANAGER);
        storePermissionsAndRole.put(storeId, permissions);
    }

    public void addItemToBasket(long basketId, long itemId, long quantity) {
        verifyIsLoggedIn();
        shoppingCart.addItemToBasket(basketId, itemId, quantity);
    }
}
