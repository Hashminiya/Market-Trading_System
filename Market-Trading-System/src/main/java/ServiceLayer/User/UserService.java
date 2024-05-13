package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;

public class UserService implements IUserService {
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
        try{
            boolean valid = facade.login(userName,password);
            String token = TokenService.generateToken(userName);
            return Response.ok(token);
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public void logout(String token){
        try{
            if(TokenService.validateToken(token)){
                facade.logout(userName);
            }
            else{
                return Response.unauthorized();
            }
        }
        catch (Exception e){
            return Response.error(e);
        }
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
