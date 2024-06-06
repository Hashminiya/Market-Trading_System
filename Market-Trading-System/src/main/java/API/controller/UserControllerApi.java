package API.controller;
import ServiceLayer.User.UserService;
import ServiceLayer.Store.StoreBuyerService;
import API.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import javax.ws.rs.core.Response;
import java.util.Date;


@RestController
public class UserControllerApi {


    private final UserService userService;

    @Autowired
    public UserControllerApi(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user/getUser")
    public ResponseEntity<UserModel> getUser(@RequestParam String userName){
        return userService.getUser(userName);
    }

    @PostMapping("/user/GuestEntry")
    public ResponseEntity<String> GuestEntry() {
        return userService.GuestEntry();
    }

    @PostMapping("/user/GuestExit")
    public ResponseEntity<String> GuestExit(@RequestParam String token) {
        return userService.GuestExit(token);
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> register(@RequestParam String userName, @RequestParam String password, @RequestParam int userAge) throws Exception{
        return userService.register(userName,password,userAge);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestParam String userName,@RequestParam String password) {
        return userService.login(userName, password);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(@RequestParam String token) {
        return userService.logout(token);
    }

    @GetMapping("/user/viewShoppingCart")
    public ResponseEntity<String> viewShoppingCart(@RequestParam String token) {
        return userService.viewShoppingCart(token);
    }

    @PatchMapping("/user/modifyShoppingCart")
    public ResponseEntity<String> modifyShoppingCart(@RequestParam String token,@RequestParam long basketId,@RequestParam long itemId,@RequestParam int newQuantity) {
        return userService.modifyShoppingCart(token, basketId, itemId, newQuantity);
    }

    @PostMapping("/user/checkoutShoppingCart")
    public ResponseEntity<String> checkoutShoppingCart(@RequestParam String token,@RequestParam String creditCard,@RequestParam Date expiryDate,@RequestParam String cvv,@RequestParam String discountCode) {
        return userService.checkoutShoppingCart(token, creditCard, expiryDate, cvv, discountCode);
    }

    @PutMapping("/user/addItemToBasket")
    public ResponseEntity<String> addItemToBasket(@RequestParam String token,@RequestParam long basketId,@RequestParam long itemId,@RequestParam int quantity) {
        return userService.addItemToBasket(token, basketId, itemId, quantity);
    }

    @PutMapping("/user/addPermission")
    public ResponseEntity<String> addPermission(@RequestParam String token,@RequestParam long storeId,@RequestParam String permission) {
        return userService.addPermission(token, storeId, permission);
    }

    @DeleteMapping("/user/removePermission")
    public ResponseEntity<String> removePermission(@RequestParam String token,@RequestParam long storeId,@RequestParam String permission) {
        return userService.removePermission(token, storeId, permission);
    }

}
