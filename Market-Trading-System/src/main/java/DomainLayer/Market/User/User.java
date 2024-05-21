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

    public User(String userName,String password, int userAge, Istate state, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.state = state;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
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

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public Istate getUserStates() { return state; }

    public boolean isLoggedIn() { return loggedIn; }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public boolean changeState(Istate state){

    }

    public boolean login(String userName, String password){

    }

    public boolean logout(String userName){

    }

    public boolean addToShoppingCart(int storeNum, int productNum, int quantity){

    }

    public boolean deleteFromShoppingCart(int storeNum, int productNum){

    }

    public boolean editShoppingCart(int storeNum, int productNum, int newQuantity){

    }

    public boolean checkOutShoppingCart(){

    }


    public String getPassword() {
        return password;

    @Override
    public String getId() {
        return userName;
    }

    @Override
    public String getName() {
        return userName;
    }
}
