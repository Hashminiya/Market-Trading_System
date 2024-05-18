package DomainLayer.Market.User;

import DomainLayer.Market.DataItem;
import DomainLayer.Market.Util.DataItem;
import jdk.jshell.spi.ExecutionControl;
import org.apache.shiro.authz.annotation.RequiresRoles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements DataItem {
    private long userID;
    private String userName;
    private int userAge;
    private boolean loggedIn;
    private ShoppingCart shoppingCart;
    private String hashedPassword;
    private String salt;
    private Set<String> roles;
    private Set<String> permissions;

    public User(){}

    public User(long userID, String userName, int userAge, String hashedPassword, String salt) {
        this.userID = userID;
        this.userName = userName;
        this.userAge = userAge;
        this.shoppingCart = new ShoppingCart();
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.roles = new HashSet<>();
        this.permissions = new HashSet<>();
        this.loggedIn = true;
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

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }


//    public boolean login(String userName, String password){
//        return true;
//    }
//
    @RequiresRoles("registered")
    public boolean logout(String userName){

    }

    public boolean addToShoppingCart(int storeID, int productNum, int quantity){

    }

    public boolean deleteFromShoppingCart(int storeNum, int productNum){
        return false;
    }

    public boolean editShoppingCart(int storeNum, int productNum, int newQuantity){
        return false;
    }

    public boolean checkOutShoppingCart(){
        return false;
    }

    // Add role and permission methods
    public void addRole(String role) {
        roles.add(role);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Long getId() {
        return userID;
    }

    @Override
    public String getName() {
        return "";
    }
}
