package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;

import javax.ws.rs.core.Response;

public class UserService implements IUserService {
    IUserFacade userFacade;
    private JwtService JwtService;

    public Response GuestEntry(){
        try{
            userFacade.CreateGuestSession();
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response GuestExit(int GuestID){
        try{
            userFacade.terminateGuest(GuestID);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response register(String userName, String password){
        try{
            userFacade.register(userName,password);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response login(String userName, String password){
        try{
            userFacade.login(userName,password);
            String token = JwtService.generateToken(userName);
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
                userFacade.logout(userName);
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
                String res = userFacade.viewShoppingCart(token);
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
                userFacade.modifyShoppingCart(token);
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
                userFacade.checkoutShoppingCart(token);
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

    public Response addItemToBasket(String token,long itemId,long quantity) {
        try{
            if(TokenService.validateToken(token)){
                userFacade.addItemToBasket(token, itemId, quantity);
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

    public Response changeUserPermission(String token, int permission) {
        try{
            if(TokenService.validateToken(token)){
                userFacade.changeUserPermission(token, permission);
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
