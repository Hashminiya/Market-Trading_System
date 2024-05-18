package DomainLayer.Market.User;

import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import Market.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class UserController implements IUserFacade{
    private static UserController userControllerInstance;
    private IRepository<String,User> users;
    private HashMap<String,ShoppingCart> carts;

    private UserController() {
        // private constructor to prevent instantiation
    }

    public static synchronized UserController getInstance() {
        if (userControllerInstance == null) {
            userControllerInstance = new UserController();
        }
        return userControllerInstance;
    }

    public UserController(IRepository<String,User> users) {
        this.users = users;
        carts = new HashMap<>();
    }

    public void createGuestSession(){
        int id = generateId();
        State g = new Guest();
        User u = new User(id,g);
        //TODO: save guest

        // users.save(id,u,null,null);
    }

    @Override
    public void terminateGuestSession() {
        //TODO implement
    }

    public void register(String userName, String password) throws Exception {
        if(users.findById(userName) != null){
            throw new Exception("username already exist");
        }
        //TODO: encrypt password
        State registered = new Registered();
        User user = users.get(userName);
        user.changeState(registered);
    }
    public boolean login(String userName, String password){
        User user = users.findById(userName);
        return user.login(userName,password);
    }

    public void logout(String token){
        String userName = token.extractUserName();
        User user = users.findById(userName);
        return user.logout(userName);
    }
    public String viewShoppingCart(String token){
        String userName = token.extractUserName();
        ShoppingCart sc = carts.get(userName);
        return sc.viewShoppingCart();
    }
    public void modifyShoppingCart(String token){

    }
    public void checkoutShoppingCart(String token){
        String userName = token.extractUserName();
        ShoppingCart shoppingCart = carts.get(userName);
        List<ItemDTO> items = new ArrayList<ItemDTO>();
        List<ShoppingBasket> baskets = shoppingCart.getBaskets();
        for(ShoppingBasket shoppingBasket: baskets){
            //TODO: call to IStoreFacade to receive the price for basket and add itemDTOs
        }
        //TODO: call checkout function in purchase

        //TODO delegate to ShoppingCart?
    }

    @Override
    public boolean checkPermission(String userName) {
        return false;
    }

    @Override
    public void assignStoreOwner(String userName, long storeId) {

    }

    @Override
    public void assignStoreManager(String userName, long storeId) {

    }

    @Override
    public List<Permission> getUserPermission(String userName) {
        return null;
    }
}
