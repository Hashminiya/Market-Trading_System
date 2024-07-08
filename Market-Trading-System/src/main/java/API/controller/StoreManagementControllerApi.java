package API.controller;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.Store.IStoreManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class StoreManagementControllerApi {

    private final IStoreManagementService storeManagementService;

    @Autowired
    public StoreManagementControllerApi(@Qualifier("StoreManagementService") IStoreManagementService storeManagementService){
        this.storeManagementService = storeManagementService;
    }

    @PostMapping("storeManagement/createStore")
    public ResponseEntity<?> createStore(@RequestParam String founderToken, @RequestParam String storeName, @RequestParam String storeDescription) {
        return storeManagementService.createStore(founderToken, storeName, storeDescription);
    }

    @PutMapping("storeManagement/addItemToStore")
    public ResponseEntity<?> addItemToStore(@RequestParam String token,@RequestParam long storeId,@RequestParam String itemName,@RequestParam String description,@RequestParam double itemPrice,@RequestParam int stockAmount,@RequestParam List<String> categories) {
        return storeManagementService.addItemToStore(token, storeId, itemName, description, itemPrice, stockAmount, categories);
    }

    @PatchMapping("storeManagement/updateItem")
    public ResponseEntity<String> updateItem(@RequestParam String token,@RequestParam long storeId,@RequestParam long itemId,@RequestParam String newName,@RequestParam double newPrice,@RequestParam int newAmount) {
        return storeManagementService.updateItem(token, storeId, itemId, newName, newPrice, newAmount);
    }

    @DeleteMapping("storeManagement/deleteItem")
    public ResponseEntity<String> deleteItem(@RequestParam String token,@RequestParam long storeId,@RequestParam long itemId) {
        return storeManagementService.deleteItem(token, storeId, itemId);
    }

    @PatchMapping("storeManagement/changeStorePolicy")
    public ResponseEntity<String> changeStorePolicy(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.changeStorePolicy(token, storeId);
    }

    @PatchMapping("storeManagement/changeDiscountType")
    public ResponseEntity<String> changeDiscountType(@RequestParam String token,@RequestParam long storeId,@RequestParam String newType) {
        return storeManagementService.changeDiscountType(token, storeId, newType);
    }

    @DeleteMapping("storeManagement/removeStore")
    public ResponseEntity<String> removeStore(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.removeStore(token, storeId);
    }

    @GetMapping("storeManagement/viewManagementInfo")
    public ResponseEntity<?> viewManagementInfo(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.viewManagementInfo(token, storeId);
    }

    @GetMapping("storeManagement/viewInventory")
    public ResponseEntity<?> viewInventory(@RequestParam String token, @RequestParam long storeId) {
        return storeManagementService.viewInventory(token, storeId);
    }

    @GetMapping("storeManagement/viewInventoryByStoreNameAndToken")
    public ResponseEntity<?> viewInventoryByStoreNameAndToken(@RequestParam String token,@RequestParam String storeName) {
        return storeManagementService.viewInventoryByStoreNameAndToken(token,storeName);
    }
    @GetMapping("storeManagement/viewCategoriesByStoreNameAndToken")
    public ResponseEntity<?> viewCategoriesByStoreNameAndToken(@RequestParam String token,@RequestParam String storeName) {
        return storeManagementService.viewCategoriesByStoreNameAndToken(token,storeName);
    }

    @PostMapping("storeManagement/addPolicyByStoreNameAndToken")
    public ResponseEntity<?> addPolicyByStoreNameAndToken(@RequestParam String token,@RequestParam String storeName, @RequestParam String policyDetails) {
        return storeManagementService.addPolicyByStoreNameAndToken(token,storeName, policyDetails);
    }

    @GetMapping("storeManagement/viewPurchasesHistory")
    public ResponseEntity<?> viewPurchasesHistory(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.viewPurchasesHistory(token, storeId);
    }

    @PostMapping("storeManagement/assignStoreOwner")
    public ResponseEntity<String> assignStoreOwner(@RequestParam String token,@RequestParam long storeId,@RequestParam String newOwnerId) {
        return storeManagementService.assignStoreOwner(token, storeId, newOwnerId);
    }

    @PostMapping("storeManagement/assignStoreManager")
    public ResponseEntity<String> assignStoreManager(@RequestParam String token,@RequestParam long storeId,@RequestParam String newManagerI,@RequestParam List<String> permissions) {
        return storeManagementService.assignStoreManager(token, storeId, newManagerI, permissions);
    }

    @PutMapping("storeManagement/addPolicy")
    public ResponseEntity<?> addPolicy(@RequestParam String token,@RequestParam long storeId,@RequestParam String policyDetails){
        return storeManagementService.addPolicy(token, storeId, policyDetails);
    }
    @GetMapping("storeManagement/viewAllPolicies")
    public ResponseEntity<?> viewAllPolicies(@RequestParam String token,@RequestParam String storeName) {
        return storeManagementService.viewAllPolicies(token, storeName);
    }

    @GetMapping("storeManagement/getStoreIdByName")
    public ResponseEntity<String> getStoreIdByName(@RequestParam String token,@RequestParam String storeName) {
        ResponseEntity<String> result = storeManagementService.getStoreIdByName(token, storeName);
        return result;
    }

   /* @PostMapping("/user/checkoutShoppingCart")
    public ResponseEntity<String> checkoutShoppingCart(@RequestParam String token, @RequestParam String creditCard, @RequestParam Date expiryDate, @RequestParam String cvv, @RequestParam String discountCode) {
        return storeManagementService.checkoutShoppingCart(token, creditCard, expiryDate, cvv, discountCode);
    }*/
}
