package DomainLayer.Market.User;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.StorePermission;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class UserController implements IUserFacade {
    private static UserController userControllerInstance;
    private final IRepository<String, User> users;
    private IStoreFacade storeFacade;
    private IPurchaseFacade purchaseFacade;
    private final BCryptPasswordEncoder passwordEncoder;
    private int guestId ;

    private UserController(IRepository<String, User> users) {

        this.users = users;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.guestId = 0;
    }

    @Override
    public void setStoreFacade(IStoreFacade storeFacadeInstance) {
        storeFacade = storeFacadeInstance;
    }

    public void setPurchaseFacade(IPurchaseFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
    }

    public static synchronized UserController getInstance(IRepository<String, User> users) {
        if (userControllerInstance == null) {
            userControllerInstance = new UserController(users);
            // TODO : We assume that when this function called, next line will be setStoreFacade..
        }
        return userControllerInstance;
    }

    @Override
    public String createGuestSession(){
        long id = generateId();
        String userName = "guest" + id;
        Istate guest = new Guest();
        User user = new User(userName, null, 0, guest, true, new ShoppingCart(new InMemoryRepository<>()));//TODO: Shopping cart should get IRepository as parameter.
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
        User user = new User(userName, encodedPassword, userAge, registered, false, new ShoppingCart(new InMemoryRepository<>()));//TODO: Shopping cart should get IRepository as parameter.
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

    @Override
    public void modifyShoppingCart(String userName, long storeId, long itemId, int newQuantity) {
        User user = getUser(userName);
        user.modifyShoppingCart(storeId, itemId, newQuantity);
    }

    @Override
    public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) throws Exception{
        User user = getUser(userName);
        List<ItemDTO> items = user.checkoutShoppingCart(this.storeFacade, discountCode);
        double totalAmount = user.getShoppingCart().getShoppingCartPrice();
        purchaseFacade.checkout(userName, creditCard, expiryDate, cvv, items, totalAmount);
        storeFacade.purchaseOccurs();
    }

    @Override
    public void assignStoreOwner(String userName, long storeId) {
        User user = getUser(userName);
        user.assignStoreOwner(storeId);
    }

    @Override
    public void assignStoreManager(String userName, long storeId,List<String> storePermissions) {
        User user = getUser(userName);
        user.assignStoreManager(storeId, storePermissions);
    }

    @Override
    public void addItemToBasket(String userName,long basketId, long itemId, int quantity) {
        User user = getUser(userName);
        user.addItemToBasket(basketId, itemId, quantity);
    }

    @Override
    public void addPermission(String userName, long storeId, String permission) {
        User user = getUser(userName);
        user.addPermission(storeId, StorePermission.valueOf(permission));
    }

    @Override
    public void removePermission(String userName, long storeId, String permission) {
        User user = getUser(userName);
        user.removePermission(storeId, StorePermission.valueOf(permission));
    }

    @Override
    public boolean checkPermission(String userName, long storeId, String permission) {
        User user = getUser(userName);
        return user.checkPermission(storeId, StorePermission.valueOf(permission));
    }

    @Override
    public List<String> getUserPermission(String userName, long storeId) {
        User user = getUser(userName);
        return user.getStorePermissions(storeId);
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

    @Override
    public boolean isRegister(String userName) {
        return getUser(userName).isRegister();
    }
}
