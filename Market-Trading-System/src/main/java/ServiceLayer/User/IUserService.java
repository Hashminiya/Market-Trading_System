package ServiceLayer.User;
import java.util.Date;
import org.springframework.http.ResponseEntity;


public interface IUserService{
    public ResponseEntity<String> GuestEntry();
    public ResponseEntity<String> GuestExit(String token);
    public ResponseEntity<String> register(String userName, String password, int userAge);
    public ResponseEntity<String> login(String userName, String password);
    public ResponseEntity<String> logout(String userName);
    public ResponseEntity<String> viewShoppingCart(String token);
    public ResponseEntity<String> modifyShoppingCart(String token, long basketId, long itemId, int newQuantity);
    public ResponseEntity<String> checkoutShoppingCart(String token, String creditCard, Date expiryDate , String cvv, String discountCode);
    public ResponseEntity<String> addItemToBasket(String token, long basketId, long itemId, int quantity);
    public ResponseEntity<String> addPermission(String token, long storeId, String permission);
    public ResponseEntity<String> removePermission(String token, long storeId, String permission);
}
