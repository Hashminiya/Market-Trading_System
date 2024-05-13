package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;

import javax.ws.rs.core.Response;

public class UserService implements IUserService {
    IUserFacade facade;

    public Response GuestEntry(){
        facade.CreateGuestSession();
    }
    public Response GuestExit(int GuestID){
        facade.terminateGuest(GuestID);
    }

    public Response register(String userName, String password){
        try{
            facade.register(userName,password);
            return Response.ok();
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public Response login(String userName, String password){
        try{
            facade.login(userName,password);
            String token = TokenService.generateToken(userName);
            return Response.ok(token);
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public Response logout(String token){
        try{
            if(TokenService.validateToken(token)){
                facade.logout(userName);
                return Response.ok();
            }
            else{
                return Response.unauthorized();
            }
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public Response viewShoppingCart(String token){
        try{
            if(TokenService.validateToken(token)){
                String res = facade.viewShoppingCart(token);
                return Response.ok(res);
            }
            else{
                return Response.unauthorized();
            }
        }
        catch (Exception e){
            return Response.error(e.getMessage());
        }
    }

    public Response modifyShoppingCart(String token){
        try{
            if(TokenService.validateToken(token)){
                facade.modifyShoppingCart(token);
                return Response.ok();
            }
            else{
                return Response.unauthorized();
            }
        }
        catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    public Response checkoutShoppingCart(String token) {
        try{
            if(TokenService.validateToken(token)){
                facade.checkoutShoppingCart(token);
                return Response.ok();
            }
            else{
                return Response.unauthorized();
            }
        }
        catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
}
