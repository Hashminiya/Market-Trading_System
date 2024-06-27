package ServiceLayer.User;
import java.util.Date;
import java.util.List;


import org.springframework.http.ResponseEntity;


public interface IUserService{
    public ResponseEntity<String> guestEntry();
    public ResponseEntity<String> guestExit(String token);
    public ResponseEntity<String> register(String userName, String password, int userAge);
    public ResponseEntity<String> login(String userName, String password);
    public ResponseEntity<String> logout(String token);
    public ResponseEntity<String> viewShoppingCart(String token);
    public ResponseEntity<String> modifyShoppingCart(String token, long basketId, long itemId, int newQuantity);
    public ResponseEntity<String> checkoutShoppingCart(String token, String creditCard, Date expiryDate , String cvv, String discountCode);
    public ResponseEntity<String> addItemToBasket(String token, long storeId, long itemId, int quantity);
    public ResponseEntity<String> addPermission(String token, String userToPermit,long storeId, String permission);
    public ResponseEntity<String> removePermission(String token, String userToUnPermit,long storeId, String permission);
    public ResponseEntity<List<Long>> viewUserStoresOwnership(String token);

    ResponseEntity<?> getShoppingCart(String token);

}
