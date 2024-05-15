package DomainLayer.Market.User;

import java.util.List;

public class User {
    private int userID;
    private String userName;
    private int userAge;
    private List<Istate> userStates;
    private boolean loggedIn;
    private ShoppingCart shoppingCart;

    public User(int userID, String userName, int userAge, List<Istate> userStates, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userID = userID;
        this.userName = userName;
        this.userAge = userAge;
        this.userStates = userStates;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public int getUserID() {
        return userID;
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
}
