package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.ws.rs.core.Response;
import java.util.Date;

public class UserService implements IUserService {
    private IUserFacade userFacade;
    private JwtService JwtService;
    private UserDetailsService userDetailsService;
    private static UserService instance;

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

    public Response GuestExit(String token){
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)) {
                userFacade.terminateGuestSession(userName);
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

    public Response register(String userName, String password, int userAge){
        try{
            userFacade.register(userName,password,userAge);
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

    @Override
    public Response modifyShoppingCart(String token, long basketId, long itemId, int newQuantity){
        try {
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && JwtService.isValid(token, userDetails)) {
                userFacade.modifyShoppingCart(userName, basketId,itemId,newQuantity);
                return Response.ok().build();
            } else {
                return Response.status(401).build();
            }
        }
        catch (Exception e){
            return Response.serverError().build();
        }
    }

    @Override
    public Response checkoutShoppingCart(String token, String creditCard, Date expiryDate , String cvv, String discountCode) {
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
                userFacade.checkoutShoppingCart(userName, creditCard, expiryDate, cvv, discountCode);
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
    public Response addItemToBasket(String token,long basketId, long itemId, int quantity) {
        try {
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && JwtService.isValid(token, userDetails)) {
                userFacade.addItemToBasket(userName,basketId, itemId, quantity);
                return Response.ok().build();
            } else {
                return Response.status(401).build();
            }
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @Override
    public Response addPermission(String token, long storeId, String permission) {
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
                userFacade.addPermission(userName, storeId, permission);
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
    public Response removePermission(String token, long storeId ,String permission) {
        try{
            String userName = JwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(userName != null && JwtService.isValid(token, userDetails)){
                userFacade.removePermission(userName, storeId,permission);
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
