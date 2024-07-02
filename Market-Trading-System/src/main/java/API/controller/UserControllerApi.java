package API.controller;
import API.Application;
import DomainLayer.Market.User.ShoppingCart;
import ServiceLayer.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
public class UserControllerApi {


    private final IUserService userService;

    @Autowired
    public UserControllerApi(@Qualifier("userService") IUserService userService){
        this.userService = userService;
    }

//    @GetMapping("/user/getUser")
//    public ResponseEntity<UserModel> getUser(@RequestParam String userName){
//        return userService.getUser(userName);
//    }

    @PostMapping("/user/GuestEntry")
    public ResponseEntity<String> GuestEntry() {
        return userService.guestEntry();
    }

    @PostMapping("/user/GuestExit")
    public ResponseEntity<String> GuestExit(@RequestParam String token) {
        return userService.guestExit(token);
    }

    @PostMapping("/user/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestParam String userName, @RequestParam String password, @RequestParam int userAge) throws Exception{
        ResponseEntity<?> r = userService.register(userName,password,userAge);
        // Extract the body and headers from the original response
//        Object body = response.getBody();
//        HttpHeaders headers = new HttpHeaders();
//
//        // Copy the original headers if necessary
//        headers.addAll(response.getHeaders());
//
//        // Set the Content-Type header to application/json
//        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
//
//        // Return the new ResponseEntity with the JSON string and updated headers
//        ResponseEntity<?> r = new ResponseEntity<>(body, headers, response.getStatusCode());
        return r;
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
    public ResponseEntity<String> checkoutShoppingCart(
            @RequestParam String token,
            @RequestParam String creditCard,
            @RequestParam String expiryDate,
            @RequestParam String cvv,
            @RequestParam String discountCode) {

        // Parse the expiryDate string into a Date object
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        Date parsedExpiryDate;
        try {
            parsedExpiryDate = sdf.parse(expiryDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid expiry date format.");
        }

        return userService.checkoutShoppingCart(token, creditCard, parsedExpiryDate, cvv, discountCode);
    }

    @PutMapping("/user/addItemToBasket")
    public ResponseEntity<String> addItemToBasket(@RequestParam String token,@RequestParam long storeId,@RequestParam long itemId,@RequestParam int quantity) {
        return userService.addItemToBasket(token, storeId, itemId, quantity);
    }

    @PutMapping("/user/addPermission")
    public ResponseEntity<String> addPermission(@RequestParam String token, @RequestParam String userToPermit,@RequestParam long storeId,@RequestParam String permission) {
        return userService.addPermission(token, userToPermit, storeId, permission);
    }

    @DeleteMapping("/user/removePermission")
    public ResponseEntity<String> removePermission(@RequestParam String token, @RequestParam String userToUnPermit, @RequestParam long storeId,@RequestParam String permission) {
        return userService.removePermission(token, userToUnPermit, storeId, permission);
    }

    @GetMapping("/user/getShoppingCart")
    public ResponseEntity<?> getShoppingCart(@RequestParam String token) {
        ResponseEntity<?> r = userService.getShoppingCart(token);
        return r;
    }

    @PutMapping("/user/viewUserStoresNamesOwnership")
    public ResponseEntity<List<Long>> viewUserStoresOwnership(@RequestParam String token) {
        return userService.viewUserStoresOwnership(token);
    }

    @PutMapping("/user/viewStoresByNameForUserOwnership")
    public ResponseEntity<List<String>> viewUserStoresNamesOwnership(@RequestParam String token) {
        return userService.viewUserStoresNamesOwnership(token);
    }

    //get shopping cart total price
    @GetMapping("/user/getShoppingCartTotalPrice")
    public ResponseEntity<Double> getShoppingCartTotalPrice(@RequestParam String token) {
        return userService.getShoppingCartTotalPrice(token);
    }
}
