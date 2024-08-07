package DomainLayer.Market.User;
import API.Utils.SpringContext;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.StorePermission;


import DomainLayer.Repositories.*;

import DomainLayer.Repositories.DbBasketRepository;
import DomainLayer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import DAL.ItemDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component("userController")
public class UserController implements IUserFacade {
    private static UserController userControllerInstance;

    private UserRepository users;
    private SystemManager admin;
    private IStoreFacade storeFacade;
    private IPurchaseFacade purchaseFacade;
    private final BCryptPasswordEncoder passwordEncoder;
    private int guestId ;
    private List<User> guests = new ArrayList<>();

    @Autowired
    public UserController(UserRepository users,
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

    public static synchronized UserController getInstance(UserRepository users, SystemManager admin, IStoreFacade storeFacade, IPurchaseFacade purchaseFacade) {
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
        return getUser(userName).getShoppingCart();
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
        BasketRepository basketRepository = SpringContext.getBean(BasketRepository.class);
        BasketItemRepository basketItemRepository = SpringContext.getBean(BasketItemRepository.class);
        User user = new User(userName, null, 0, guest, true, new ShoppingCart(basketRepository, basketItemRepository));//TODO: Shopping cart should get IRepository as parameter.
        guests.add(user);
        return userName;
    }

    @Override
    public void terminateGuestSession(String userName) {
        guests.remove(getUser(userName));
    }

    public void register(String userName,String password, int userAge) throws Exception {
        if(users.existsById(userName)){
            throw new Exception("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Istate registered = new Registered();
        BasketRepository baskets = SpringContext.getBean(BasketRepository.class);
        BasketItemRepository basketItemRepository = SpringContext.getBean(BasketItemRepository.class);
        User user = new User(userName, encodedPassword, userAge, registered, false, new ShoppingCart(baskets, basketItemRepository));//TODO: Shopping cart should get IRepository as parameter.
        users.save(user);
    }

    public boolean login(String userName, String rawPassword) throws Exception {
        User user = getUser(userName);
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            boolean result = user.login();
            users.save(user);
            return result;
        }
        throw new IllegalArgumentException("wrong password");
    }

    public void logout(String userName) {
        User user = getUser(userName);
        user.logout();
        users.save(user);
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
        List<ShoppingBasket> baskets = getUser(userName).getShoppingCart().getBaskets();
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
            Set<String> assignersCopy = new HashSet<>(assigners);
            assignersCopy.add(assignerUser.getId());
            assigneeUser.lock();
            assigneeUser.setAssigners(storeId, assignersCopy);
            assigneeUser.unlock();
        }
        else{
            assigneeUser.setAssigners(storeId, new HashSet<>());
        }
        assigneeUser.assignStoreOwner(storeId);
        users.save(assigneeUser);
    }

    @Override
    public void assignStoreManager(String assigner, String assignee, long storeId,List<String> storePermissions) {
        User assigneeUser = getUser(assignee);
        User assignerUser = getUser(assigner);
        Set<String> assigners = assignerUser.getAssigners(storeId);
        Set<String> assignersCopy = new HashSet<>(assigners);
        assignersCopy.add(assignerUser.getId());
        assigneeUser.lock();
        assigneeUser.setAssigners(storeId, assignersCopy);
        assigneeUser.unlock();
        assigneeUser.assignStoreManager(storeId, storePermissions);
        users.save(assigneeUser);
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

    @Transactional
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
        Optional<User> user = users.findById(userName);
        if (user.isEmpty()) {
            User guest = getGuest(userName);
            if (guest == null) {
                throw new IllegalArgumentException("user not exists");
            }
            return guest;
        }
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = getUser(userName);
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
        return getUser(userName).getUserAge();
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

    private User getGuest(String userName){
        for (User guest : guests){
            if (guest.getUserName().equals(userName)){
                return guest;
            }
        }
        return null;
    }

    @Override
    public double getShoppingCartTotalPrice(String userName) {
        return getUser(userName).getShoppingCart().getShoppingCartPrice();
    }

    public List<String> getGuests(){
        List<String> guestsNames = new ArrayList<>();
        for (User guest : guests){
            guestsNames.add(guest.getUserName());
        }
        return guestsNames;
    }

    public void setUserRepository(UserRepository userRepository) { users = userRepository;}

}
