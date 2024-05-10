package Market.User;

public class UserService implements IUserService{
    IUserFacade facade;

    public void GuestEntry(){
        facade.CreateGuestSession();
    }
    public void GuestExit(int GuestID){
        facade.terminateGuest(GuestID);
    }

    public void register(String userName, String password){
        facade.register(userName,password);
    }

    public void login(String userName, String password){
        facade.login(userName,password);
        //TODO: if username and passwords are valid, create and return a token
    }

    public void logout(String userName){
        facade.logout(userName);
    }

    public String viewShoppingCart(String token){
        //TODO: Authentication
        facade.viewShoppingCart(token);
        return "";
    }

    public void modifyShoppingCart(String token){
        //TODO: Authentication
        facade.modifyShoppingCart(token);
    }
    public void checkoutShoppingCart(String token) {
        //TODO: Authentication
        facade.checkoutShoppingCart(token);
    }
}
