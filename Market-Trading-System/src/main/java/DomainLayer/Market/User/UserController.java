package DomainLayer.Market.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.StorePermission;
import static DomainLayer.Market.Util.IdGenerator.generateId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserController implements IUserFacade {

    private final IRepository<String, User> users;
    private final HashMap<String, ShoppingCart> carts;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(IRepository<String, User> users) {
        this.users = users;
        carts = new HashMap<>();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createGuestSession(){
        Long id = generateId();
        String userName = "guest" + id;
        Istate guest = new Guest();
        User user = new User(userName, null,null,guest,false,new ShoppingCart());
        users.save(user);
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
        //TODO: delete guest when register
    }

    public boolean login(String userName, String rawPassword) throws Exception {
        User user = users.findById(userName);
        if (user == null) {
            throw new Exception("user not exists");
        }
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            user.login(userName, rawPassword);
            return true;
        }
        throw new Exception("password is incorrect");
    }

    public void logout(String token) {
        String userName = token.extractUserName();
        User user = users.findById(userName);
        user.logout(userName);
    }

    public String viewShoppingCart(String token) {
        String userName = token.extractUserName();
        ShoppingCart sc = carts.get(userName);
        return sc.viewShoppingCart();
    }

    public void modifyShoppingCart(String token) {
        //TODO implement
    }

    public void checkoutShoppingCart(String token) {
        String userName = token.extractUserName();
        ShoppingCart shoppingCart = carts.get(userName);
        List<ItemDTO> items = new ArrayList<>();
        List<ShoppingBasket> baskets = shoppingCart.getBaskets();
        for (ShoppingBasket shoppingBasket : baskets) {
            //TODO: call to IStoreFacade to receive the price for basket and add itemDTOs
        }
        //TODO: call checkout function in purchase
        //TODO delegate to ShoppingCart?
    }

    @Override
    public boolean checkPermission(String userName) {
        return false;
    }

    public boolean checkPermission(String userName, Long storeId) {
        return false;
    }

    @Override
    public void assignStoreOwner(String userName, long storeId) {
        //TODO implement
    }

    @Override
    public void assignStoreManager(String userName, long storeId) {
        //TODO implement
    }

    @Override
    public List<StorePermission> getUserPermission(String userName) {
        return null;
    }


    private int generateId() {
        // Implement ID generation logic
        return 0;
    }
}
