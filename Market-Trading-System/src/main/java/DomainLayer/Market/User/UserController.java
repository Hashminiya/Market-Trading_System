package DomainLayer.Market.User;

import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.StorePermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DomainLayer.Market.Util.IdGenerator.generateId;

public class UserController implements IUserFacade{

    private final IRepository<String,User> users;
    private final HashMap<String,ShoppingCart> carts;

    public UserController(IRepository<String,User> users) {
        this.users = users;
        carts = new HashMap<>();
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

    public void register(String userName, String password) throws Exception {
        if(users.findById(userName) != null){
            throw new Exception("username already exist");
        }
        //TODO: encrypt password
        Istate registered = new Registered();
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

    public boolean checkPermission(String userName, Long storeId) {
        return false;
    }

    @Override
    public void assignStoreOwner(String userName, long storeId) {

    }

    @Override
    public void assignStoreManager(String userName, long storeId) {

    }

    @Override
    public List<StorePermission> getUserPermission(String userName) {
        return null;
    }
}
