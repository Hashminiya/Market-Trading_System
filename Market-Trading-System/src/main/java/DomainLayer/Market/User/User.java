package DomainLayer.Market.User;

import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.StorePermission;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User implements DataItem<String> {
    private String userName;
    private String password;
    private int userAge;
    private Istate state;
    private boolean loggedIn;
    private ShoppingCart shoppingCart;
    private Map<Long, Set<StorePermission>> storePermissions;

    public User(String userName, String password, int userAge, Istate state, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.state = state;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
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

    public boolean isLoggedIn() {
        return loggedIn;
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

    public boolean login(String userName, String password) {
        if (this.userName.equals(userName) && this.password.equals(password)) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    public boolean logout(String userName) {
        if (this.userName.equals(userName)) {
            loggedIn = false;
            return true;
        }
        return false;
    }

    public boolean addToShoppingCart(int storeNum, int productNum, int quantity) {
        if (loggedIn) {
            return shoppingCart.addToShoppingCart(storeNum, productNum, quantity);
        }
        return false;
    }

    public boolean deleteShoppingBasket(int storeNum, int productNum) {
        if (loggedIn) {
            return shoppingCart.deleteShoppingBasket(storeNum, productNum);
        }
        return false;
    }

    public boolean modifyShoppingCart(int storeNum, int productNum, int newQuantity) {
        if (loggedIn) {
            return shoppingCart.modifyShoppingCart(storeNum, productNum, newQuantity);
        }
        return false;
    }

    public boolean checkOutShoppingCart() {
        if (loggedIn) {
            return shoppingCart.checkOutShoppingCart();
        }
        return false;
    }
}
