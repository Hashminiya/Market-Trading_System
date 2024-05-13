package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;

import javax.ws.rs.core.Response;

public class UserService implements IUserService {
    IUserFacade facade;

    public Response GuestEntry(){
        try{
            facade.CreateGuestSession();
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response GuestExit(int GuestID){
        try{
            facade.terminateGuest(GuestID);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response register(String userName, String password){
        try{
            facade.register(userName,password);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response login(String userName, String password){
        try{
            facade.login(userName,password);
            String token = TokenService.generateToken(userName);
            return Response.ok(token).build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response logout(String token){
        try{
            if(TokenService.validateToken(token)){
                String userName = TokenService.extractUserName(token);
                facade.logout(userName);
                return Response.ok().build();
            }
            else{
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response viewShoppingCart(String token){
        try{
            if(TokenService.validateToken(token)){
                String res = facade.viewShoppingCart(token);
                return Response.ok(res).build();
            }
            else{
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response modifyShoppingCart(String token){
        try{
            if(TokenService.validateToken(token)){
                facade.modifyShoppingCart(token);
                return Response.ok().build();
            }
            else{
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }
    public Response checkoutShoppingCart(String token) {
        try{
            if(TokenService.validateToken(token)){
                facade.checkoutShoppingCart(token);
                return Response.ok().build();
            }
            else{
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }
}
