package DomainLayer.Market.User;

import Market.IRepository;

import java.util.HashMap;

public class UserController implements IUserFacade{
    //private IRepository<User> users;
    HashMap<String,User> users;

    public UserController() {
        users = new HashMap<>();
    }

    public void createGuestSession(){
        int id = generateId();
        State g = new Guest();
        User u = new User(id,g);
        //TODO: save guest

        // users.save(id,u,null,null);
    }
    public void terminateGuestSession(String token){
        int id = token.extractId();
        User u = users.findById(id);
        users.delete(u);
    }

    public void register(String userName, String password) throws Exception {
        //TODO: check if the username already exist?
        if(users.containsKey(userName)){
            throw new Exception("username already exist");
        }
        //TODO: encrypt password
        State registered = new Registered();
        User user = users.get(userName);
        user.changeState(registered);
    }
    public boolean login(String userName, String password){
        User u = users.get(userName);
        return u.login(userName,password);
    }

    public void logout(String userName){
        int id = token.extractId();
        User u = users.findById(id);
        return u.logout(userName);
    }
    public String viewShoppingCart(String token){
        int id = token.extractId();
        ShoppingCart sc = //TODO: get ShoppingCart
        return sc.viewShoppingCart();
    }
    public void modifyShoppingCart(String token){

    }
    public void checkoutShoppingCart(String token){

    }
}
