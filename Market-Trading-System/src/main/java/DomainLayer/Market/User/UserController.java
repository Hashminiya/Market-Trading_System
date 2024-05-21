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
        User user = new User(userName, null, 0, guest, true, new ShoppingCart());
        users.save(user);
        return userName;
    }

    @Override
    public void terminateGuestSession(String userName) {
        users.delete(userName);
    }

    @Override
    public void register(String userName, String password) {

    }

    public void register(String userName,String password, int userAge) throws Exception {
        if (users.findById(userName) != null) {
            throw new Exception("username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Istate registered = new Registered();
        User user = new User(userName, encodedPassword, userAge, registered, false, new ShoppingCart());
        users.save(user);
        //TODO: Shopping cart should get IRepository as parameter.
        //TODO: delete guest when register.
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
    public void assignStoreOwner(String userName, long storeId, String newOwnerName, List<String> storePermissions) {
        User user = getUser(userName);
        if (user.checkPermission(storeId, StorePermission.ASSIGN_OWNER)) {
            User newOwner = getUser(newOwnerName);
            newOwner.assignStoreOwner(storeId, storePermissions);
        }
        else {
            throw new IllegalArgumentException("user can't assign owner");
        }
    }

    @Override
    public void assignStoreManager(String userName, long storeId, String newManagerName, List<String> userPermissions) {
        User user = getUser(userName);
        if (user.checkPermission(storeId, StorePermission.ASSIGN_MANAGER)) {
            User newManager = getUser(newManagerName);
            newManager.assignStoreManager(storeId, userPermissions);
        }
        else {
            throw new IllegalArgumentException("user can't assign manager");
        }

    }

    @Override
    public List<String> getStorePermission(String userName, long storeId) {
        User user = getUser(userName);
        //get store permissions from user and return them as list of strings
        return user.getStorePermissions(storeId);
    }


    @Override
    public void terminateGuest(int guestID) {


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
}
