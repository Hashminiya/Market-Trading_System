package DomainLayer.Market.User;

import Market.IRepository;

public class UserController implements IUserFacade{
    private IRepository<User> users;

    public UserController() {
        users = new UsersRepository();
    }

    public void createGuestSession(){
        int id = generateId();
        State g = new Guest();
        User u = new User(id,g);
        users.save(id,u,null,null);
    }
    public void terminateGuestSession(String token){
        int id = token.extractId();
        User u = users.findById(id);
        users.delete(u);
    }

    public void register(String token, String userName, String password){
        //TODO: check if the username already exist?
        int id = token.extractId();
        State r = new Registered();
        User u = users.findById(id);
        u.changeState(r);
    }
    public boolean login(String token, String userName, String password){
        int id = token.extractId();
        User u = users.findById(id);
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
