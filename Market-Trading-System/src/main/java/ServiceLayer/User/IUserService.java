package ServiceLayer.User;

import javax.ws.rs.core.Response;
import java.util.Date;
import org.springframework.http.ResponseEntity;


public interface IUserService{
    public Response GuestEntry();
    public Response GuestExit(String token);
    public ResponseEntity<String> register(String userName, String password, int userAge);
    public Response login(String userName, String password);
    public Response logout(String userName);
    public Response viewShoppingCart(String token);
    public Response modifyShoppingCart(String token, long basketId, long itemId, int newQuantity);
    public Response checkoutShoppingCart(String token, String creditCard, Date expiryDate , String cvv, String discountCode);
    public Response addItemToBasket(String token, long basketId, long itemId, int quantity);
    public Response addPermission(String token, String userToPermit,long storeId, String permission);
    public Response removePermission(String token, String userToUnPermit,long storeId, String permission);
}
