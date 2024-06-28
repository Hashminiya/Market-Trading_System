package DomainLayer.Market.User;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.StorePermission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("userController")
public class UserController implements IUserFacade {
    private static UserController userControllerInstance;
    private IRepository<String, User> users;
    private SystemManager admin;
    private IStoreFacade storeFacade;
    private IPurchaseFacade purchaseFacade;
    private final BCryptPasswordEncoder passwordEncoder;
    private int guestId ;

    @Autowired
    private UserController(@Qualifier("InMemoryRepository") IRepository<String, User> users,
                           @Qualifier("SystemManager") SystemManager admin,
                           @Qualifier("StoreController") IStoreFacade storeFacade,
                           @Qualifier("purchaseController") IPurchaseFacade purchaseFacade) {
        this.users = users;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.guestId = 0;
        this.admin = admin;
        this.storeFacade = storeFacade;
        this.purchaseFacade = purchaseFacade;
    }

    public static synchronized UserController getInstance(IRepository<String, User> users, SystemManager admin, IStoreFacade storeFacade, IPurchaseFacade purchaseFacade) {
        if (userControllerInstance == null) {
            userControllerInstance = new UserController(users, admin, storeFacade, purchaseFacade);
            // TODO : We assume that when this function called, next line will be setStoreFacade..
        }
        return userControllerInstance;
    }

    public void clear() {
        userControllerInstance = null;
        storeFacade = null;
        purchaseFacade.clear();
        users = null;
        admin = null;
    }

    @Override
    public ShoppingCart getShoppingCart(String userName) {
        return users.findById(userName).getShoppingCart();
    }


    @Override
    public void setStoreFacade(IStoreFacade storeFacadeInstance) {
        storeFacade = storeFacadeInstance;
    }

    public void setPurchaseFacade(IPurchaseFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
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

    public String viewShoppingCart(String userName) throws Exception{
        User user = getUser(userName);
        return user.getShoppingCart().viewShoppingCart(this.storeFacade);
    }

    @Override
    public void modifyShoppingCart(String userName, long storeId, long itemId, int newQuantity) {
        User user = getUser(userName);
        user.modifyShoppingCart(storeId, itemId, newQuantity);
    }

    @Override
    public void checkoutShoppingCart(String userName, String creditCard, Date expiryDate , String cvv, String discountCode) throws Exception{
        User user = getUser(userName);
        List<ShoppingBasket> baskets = users.findById(userName).getShoppingCart().getBaskets();
        List<ItemDTO> items = user.checkoutShoppingCart(this.storeFacade, discountCode);
        double totalAmount = user.getShoppingCart().getShoppingCartPrice();
        try {
            purchaseFacade.checkout(userName, creditCard, expiryDate, cvv, items, totalAmount);
        }
        catch (Exception e) {
            storeFacade.restoreStock(baskets);
            throw new Exception(e.getMessage());
        }
        storeFacade.purchaseOccurs(baskets); //change this function to clear items cache
        user.clearShoppingCart();
    }

    @Override
    public void assignStoreOwner(String assigner, String assignee, long storeId) {
        User assigneeUser = getUser(assignee);
        if(!assigner.equals(assignee)) {
            User assignerUser = getUser(assigner);
            Set<String> assigners = assignerUser.getAssigners(storeId);
            assigners.add(assignerUser.getId());
            assigneeUser.setAssigners(storeId, assigners);
        }
        else{
            assigneeUser.setAssigners(storeId, new HashSet<>());
        }
        assigneeUser.assignStoreOwner(storeId);
    }

    @Override
    public void assignStoreManager(String assigner, String assignee, long storeId,List<String> storePermissions) {
        User assigneeUser = getUser(assignee);
        User assignerUser = getUser(assigner);
        Set<String> assigners = assignerUser.getAssigners(storeId);
        assigners.add(assignerUser.getId());
        assigneeUser.setAssigners(storeId, assigners);
        assigneeUser.assignStoreManager(storeId, storePermissions);
    }

    @Override
    public Long addItemToBasket(String userName,long storeId, long itemId, int quantity) throws Exception {
        User user = getUser(userName);
        return user.addItemToBasket(storeId, itemId, quantity, storeFacade);
    }

    @Override
    public void addPermission(String userName, String userToPermit,long storeId, String permission) {
        User user = getUser(userToPermit);
        if(user.getAssigners(storeId).contains(userName)) {
            user.lock();
            user.addPermission(storeId, StorePermission.valueOf(permission));
            user.unlock();
        }
        else {
            throw new RuntimeException("User can't assign permissions to this user.");
        }
    }

    @Override
    public void removePermission(String userName, String userToUnPermit,long storeId, String permission) {
        User user = getUser(userToUnPermit);
        if(user.getAssigners(storeId).contains(userName)) {
            user.lock();
            user.removePermission(storeId, StorePermission.valueOf(permission));
            user.unlock();
        }
        else {
            throw new RuntimeException("User can't assign permissions to this user.");
        }
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
    public UserDetails loadUserByUsername(String userName) {
        User user = users.findById(userName);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUserName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };
    }

    @Override
    public boolean isRegister(String userName) {
        return getUser(userName).isRegister();
    }

    @Override
    public boolean isAdmin(String userName) {
        return admin.getUserName().equals(userName);
    }

    @Override
    public int getUserAge(String userName) {
        return users.findById(userName).getUserAge();
    }

    public List<Long> viewUserStoresOwnership(String userName){
        User user = getUser(userName);
        return user.viewUserStoresOwnership();
    }

    public List<String> viewUserStoresNamesOwnership(String userName){
        User user = getUser(userName);
        List<Long> storesId = user.viewUserStoresOwnership();
        return storeFacade.getListOfStorNamesByIds(storesId);
    }

}
