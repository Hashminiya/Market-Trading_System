package API.controller;
import ServiceLayer.User.UserService;
import API.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserControllerApi {


    private final UserService userService;

    @Autowired
    public UserControllerApi(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user")
    public UserModel getUser(@RequestParam String userName){
        return userService.getUser(userName);
    }

    @GetMapping("user/GuestEntry")
    public Response GuestEntry() {
        return userService.GuestEntry();
    }

    @GetMapping("user/GuestExit")
    public Response GuestExit(@RequestParam String token) {
        return userService.GuestExit(token);
    }

    @GetMapping("/user/register")
    public Response register(@RequestParam String userName,@RequestParam String password,@RequestParam int userAge) throws Exception{
        return userService.register(userName,password,userAge);
    }

    @GetMapping("user/login")
    public Response login(@RequestParam String userName,@RequestParam String password) {
        return userService.login(userName, password);
    }

    @GetMapping("user/logout")
    public Response logout(@RequestParam String token) {
        return userService.logout(token);
    }

    @GetMapping("user/viewShoppingCart")
    public Response viewShoppingCart(@RequestParam String token) {
        return userService.viewShoppingCart(token);
    }

    @GetMapping("user/modifyShoppingCart")
    public Response modifyShoppingCart(@RequestParam String token,@RequestParam long basketId,@RequestParam long itemId,@RequestParam int newQuantity) {
        return userService.modifyShoppingCart(token, basketId, itemId, newQuantity);
    }

    @GetMapping("user/checkoutShoppingCart")
    public Response checkoutShoppingCart(@RequestParam String token,@RequestParam String creditCard,@RequestParam Date expiryDate,@RequestParam String cvv,@RequestParam String discountCode) {
        return userService.checkoutShoppingCart(token, creditCard, expiryDate, cvv, discountCode);
    }

    @GetMapping("user/addItemToBasket")
    public Response addItemToBasket(@RequestParam String token,@RequestParam long basketId,@RequestParam long itemId,@RequestParam int quantity) {
        return userService.addItemToBasket(token, basketId, itemId, quantity);
    }

    @GetMapping("user/addPermission")
    public Response addPermission(@RequestParam String token,@RequestParam long storeId,@RequestParam String permission) {
        return userService.addPermission(token, storeId, permission);
    }

    @GetMapping("user/removePermission")
    public Response removePermission(@RequestParam String token,@RequestParam long storeId,@RequestParam String permission) {
        return userService.removePermission(token, storeId, permission);
    }

}
