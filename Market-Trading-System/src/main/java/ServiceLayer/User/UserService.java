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

    public Response<String> register(String userName, String password){
        try{
            facade.register(userName,password);
            return Response.ok();
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public Response<String> login(String userName, String password){
        try{
            facade.login(userName,password);
            String token = TokenService.generateToken(userName);
            return Response.ok(token);
        }
        catch (Exception e){
            return Response.error(e);
        }
    }

    public Response<String> logout(String token){
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

    public Response<String> viewShoppingCart(String token){
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

    public Response<String> modifyShoppingCart(String token){
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
    public Response<String> checkoutShoppingCart(String token) {
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
