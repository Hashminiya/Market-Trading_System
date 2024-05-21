package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.ws.rs.core.Response;

public class UserService implements IUserService {
    IUserFacade userFacade;
    private JwtService JwtService;
    private UserDetailsService userDetailsService;
    private static UserService instance;
    private IUserFacade userFacade;

    private UserService(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public static synchronized UserService getInstance(IUserFacade userFacade) {
        if (instance == null) {
            instance = new UserService(userFacade);
        }
        return instance;
    }

    public Response GuestEntry(){
        try{
            String userName =  userFacade.createGuestSession();
            String token = JwtService.generateToken(userName, "GUEST");
            return Response.ok(token).build();
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
            String token = JwtService.generateToken(userName, "REGISTERED");
            return Response.ok(token).build();
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response logout(String token){
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
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
        //implement using JwtService:
        try {
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && JwtService.isValid(token, userDetails)) {
                return Response.ok(userFacade.viewShoppingCart(userName)).build();
            } else {
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response modifyShoppingCart(String token){
        try {
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && JwtService.isValid(token, userDetails)) {
                userFacade.modifyShoppingCart(token);
                return Response.ok().build();
            } else {
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    public Response checkoutShoppingCart(String token) {
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
                userFacade.checkoutShoppingCart(userName);
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

    @Override
    public Response addItemToBasket(String token, long itemId, long quantity) {
        try {
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && JwtService.isValid(token, userDetails)) {
                userFacade.addItemToBasket(userName, itemId, quantity);
                return Response.ok().build();
            } else {
                return Response.status(401).build();
            }
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @Override
    public Response changeUserPermission(String token, int permission) {
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
                userFacade.changeUserPermission(userName,permission);
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
