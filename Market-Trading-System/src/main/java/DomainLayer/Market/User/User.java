package DomainLayer.Market.User;

import DomainLayer.Market.Util.DataItem;

import java.util.List;

public class User implements DataItem {
    private String userName;
    private String password;
    private int userAge;
    private List<Istate> userStates;
    private boolean loggedIn;
    private ShoppingCart shoppingCart;

    public User(String userName,String password, int userAge, List<Istate> userStates, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.userStates = userStates;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserStates(List<Istate> userStates) {
        this.userStates = userStates;
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

    public List<Istate> getUserStates() {
        return userStates;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

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
    }
}
