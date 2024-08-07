package DomainLayer.Market.User;

import API.Utils.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Converters.StoreEnumSetConverter;
import DomainLayer.Converters.StringSetConverter;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Component
@Scope("prototype")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements IUser,DataItem<String> {
    @Id
    private String userName;
    private String password;
    private int userAge;
    @Transient
    private Istate state;
    @Getter
    protected boolean loggedIn;
    @Transient
    private ShoppingCart shoppingCart;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_assigners", joinColumns = @JoinColumn(name = "user_name"))
    @MapKeyColumn(name = "store_id")
    @Column(name = "assigners")
    @Convert(converter = StringSetConverter.class)
    @BatchSize(size = 25)
    private Map<Long, Set<String>> assigners;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_store_permissions", joinColumns = @JoinColumn(name = "user_name"))
    @MapKeyColumn(name = "store_id")
    @Column(name = "permission", length = 500)
    @Convert(converter = StoreEnumSetConverter.class)
    @BatchSize(size = 25)
    private Map<Long, Set<StoreEnum>> storePermissionsAndRole;
    @Transient
    private ReentrantLock lock;

    public User(String userName, String password, int userAge, Istate state, boolean loggedIn, ShoppingCart shoppingCart) {
        this.userName = userName;
        this.password = password;
        this.userAge = userAge;
        this.state = state;
        this.loggedIn = loggedIn;
        this.shoppingCart = shoppingCart;
        this.storePermissionsAndRole = new HashMap<>();
        this.assigners = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public User() {

    }

    @PostLoad
    private void initFields() {
        //init state
        this.state = (Istate) SpringContext.getBean("registered");
        //load shopping cart
        this.shoppingCart = (ShoppingCart) SpringContext.getBean("shoppingCart");

        List<ShoppingBasket> baskets = shoppingCart.loadBasketsForUser(userName);
        shoppingCart.setShoppingBaskets(baskets);

        //this.assigners = new HashMap<>();

        this.lock = new ReentrantLock();
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

    public Set<String> getAssigners(long storeId) {
        return assigners.getOrDefault(storeId, new HashSet<>());
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

    public void setAssigners(long storeId, Set<String> newAssigners) {
        if(!this.assigners.containsKey(storeId))
            assigners.put(storeId, newAssigners);
        else{
            throw new RuntimeException("The user already assigned to store management");
        }
    }


    public boolean login() {
        if (loggedIn) {
            throw new IllegalArgumentException("user already logged in");
        }
        loggedIn = true;
        return true;
    }

    public void logout() {
        verifyIsLoggedIn();
        loggedIn = false;
    }

    public void deleteShoppingBasket(long basketId) {
        verifyIsLoggedIn();
        shoppingCart.deleteShoppingBasket(basketId);
    }

    public void modifyShoppingCart(long basketId, long itemId, int newQuantity) {
        verifyIsLoggedIn();
        shoppingCart.modifyShoppingCart(basketId, itemId, newQuantity);
    }

    private void verifyIsLoggedIn() {
        if (!loggedIn) {
            throw new IllegalArgumentException("user not logged in");
        }
    }

    public List<String> getStorePermissions(long storeId) {
        List<String> permissions = new ArrayList<>();
        if (storePermissionsAndRole.containsKey(storeId)) {
            for (StoreEnum permission : storePermissionsAndRole.get(storeId)) {
                permissions.add(permission.name());
            }
        }
        return permissions;
    }

    public boolean checkPermission(long storeId, StorePermission storePermission) {
        if (storePermissionsAndRole.containsKey(storeId)) {
            if (storePermissionsAndRole.get(storeId).contains(storePermission)) {
                return true;
            }
            throw new IllegalArgumentException("user does not have " + storePermission.name() + " permission for this store");
        }
        throw new IllegalArgumentException("user does not have any permissions for this store");
    }

    public List<ItemDTO> checkoutShoppingCart(IStoreFacade storeFacade, String discountCode) throws Exception{
        verifyIsLoggedIn();
        return shoppingCart.checkoutShoppingCart(userName, storeFacade, discountCode);
    }

    public void assignStoreOwner(long storeId) {
        if(storePermissionsAndRole.containsKey(storeId) && storePermissionsAndRole.get(storeId).contains(StoreRole.OWNER)){
            throw new IllegalArgumentException("user is already store owner");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        permissions.add(StoreRole.OWNER); //Pay attention that if the user is already a manager, he will be both a manager and an owner!
        permissions.addAll(Arrays.asList(StorePermission.values()));
        storePermissionsAndRole.put(storeId, permissions);
    }

    public void assignStoreManager(long storeId, List<String> userPermissions) {
        if(storePermissionsAndRole.containsKey(storeId) && storePermissionsAndRole.get(storeId).contains(StoreRole.MANAGER)){
            throw new IllegalArgumentException("user is already store manager");
        }
        Set<StoreEnum> permissions = new HashSet<>();
        permissions.add(StoreRole.MANAGER);
        for (String permission : userPermissions) {
            permissions.add(StorePermission.valueOf(permission));
        }
        storePermissionsAndRole.put(storeId, permissions);
    }

    public Long addItemToBasket(long storeId, long itemId, int quantity, IStoreFacade storeFacade) throws Exception {
        verifyIsLoggedIn();
        return shoppingCart.addItemBasket(storeId, itemId, quantity, storeFacade, userName);

    }

    public boolean isRegister() {
        return state.isRegistered();
    }

    public void removePermission(long storeId, StorePermission storePermission) {
        storePermissionsAndRole.get(storeId).remove(storePermission);
    }

    public void addPermission(long storeId, StorePermission storePermission) {
        if (!storePermissionsAndRole.containsKey(storeId)) {
            storePermissionsAndRole.put(storeId, new HashSet<>());
            assigners.put(storeId, new HashSet<>());
        }
        storePermissionsAndRole.get(storeId).add(storePermission);
    }
    public void clearShoppingCart(){
        this.shoppingCart.clear();
    }

    public List<Long> viewUserStoresOwnership() {
        List<Long> ownedStoreIds = new ArrayList<>();

        for (Map.Entry<Long, Set<StoreEnum>> entry : storePermissionsAndRole.entrySet()) {
            Long storeId = entry.getKey();
            Set<StoreEnum> roles = entry.getValue();

            // Check if the user is an OWNER of this store
            if (roles.contains(StoreRole.OWNER)) {
                ownedStoreIds.add(storeId);
            }
        }
        return ownedStoreIds;
    }

    public void lock(){ lock.lock();}

    public void unlock() {lock.unlock();}
}
