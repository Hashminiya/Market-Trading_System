package DomainLayer.Market.User;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.StorePermission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class UserController implements IUserFacade {
    private final IRepository<String, User> users;
    private final IStoreFacade storeFacade;
    private final IPurchaseFacade purchaseFacade;
    private final BCryptPasswordEncoder passwordEncoder;

    private int guestId ;

    public UserController(IRepository<String, User> users, IStoreFacade storeFacade, IPurchaseFacade purchaseFacade) {
        this.users = users;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.guestId = 0;
        this.storeFacade = storeFacade;
        this.purchaseFacade = purchaseFacade;
    }

    @Override
    public String createGuestSession(){
        long id = generateId();
        String userName = "guest" + id;
        Istate guest = new Guest();
        User user = new User(userName, null, 0, guest, true, new ShoppingCart());//TODO: Shopping cart should get IRepository as parameter.
        users.save(user);
        return userName;
    }

    @Override
    public void terminateGuestSession(String userName) {
        users.delete(userName);
    }

    public void register(String userName,String password, int userAge) throws Exception {
        if (users.findById(userName) != null) {
            throw new Exception("username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Istate registered = new Registered();
        User user = new User(userName, encodedPassword, userAge, registered, false, new ShoppingCart());//TODO: Shopping cart should get IRepository as parameter.
        users.save(user);
    }

    public boolean login(String userName, String rawPassword) throws Exception {
        User user = getUser(userName);
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user.login();
        }
        throw new IllegalArgumentException("wrong password");
    }

    public void logout(String userName) {
        User user = getUser(userName);
        user.logout();
    }

    public String viewShoppingCart(String userName) {
        User user = getUser(userName);
        return user.getShoppingCart().viewShoppingCart();
    }

    public void modifyShoppingCart(String userName, int storeNum, int productNum, int newQuantity) {
        User user = getUser(userName);
        user.modifyShoppingCart(storeNum, productNum, newQuantity);
    }

    public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) {
        User user = getUser(userName);
        List<ItemDTO> items = user.checkoutShoppingCart(this.storeFacade, discountCode);
        double totalAmount = 0;//TODO: call method to calculate total amount
        purchaseFacade.checkout(userName, creditCard, expiryDate, cvv, items, totalAmount);
        storeFacade.purchaseOccurs();
    }

    @Override
    public void assignStoreOwner(String newOwnerName, long storeId) {
        User newOwner = getUser(newOwnerName);
        newOwner.assignStoreOwner(storeId);
    }

    @Override
    public void assignStoreManager(String newOwnerName, long storeId,List<String> storePermissions) {
        User newManager = getUser(newOwnerName);
        newManager.assignStoreManager(storeId, storePermissions);
    }

    @Override
    public void terminateGuest(int guestID) {
        //TODO: implement
    }

    @Override
    public void addItemToBasket(String userName,long basketId, long itemId, long quantity) {
        User user = getUser(userName);
        user.addItemToBasket(basketId, itemId, quantity);
    }

    @Override
    public void changeUserPermission(String userName, int permission) {

    }

    private int generateId() {
        return guestId++;
    }

    private User getUser(String userName) {
        User user = users.findById(userName);
        if (user == null) {
            throw new IllegalArgumentException("user not exists");
        }
        return user;
    }

    public boolean isRegistered(String userName) {
        return getUser(userName).isRegistered();
    }
}
