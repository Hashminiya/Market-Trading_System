package Market.User;

import Market.IRepository;

public class UserController implements IUserFacade{
    IRepository<User> users;

    public UserController() {
    }

    public void createGuestSession(){

    }
    public void terminateGuestSession(){

    }
    public void register(String userName, String password){

    }
    public boolean login(String userName, String password){

    }
    public void logout(String userName){

    }
    public String viewShoppingCart(String token){

    }
    public void modifyShoppingCart(String token){

    }
    public void checkoutShoppingCart(String token){

    }
}