package API.controller;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Util.IRepository;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.Store.IStoreManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
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
    public ResponseEntity<String> createStore(@RequestParam String founderToken, @RequestParam String storeName, @RequestParam String storeDescription) {
        return storeManagementService.createStore(founderToken, storeName, storeDescription);
    }

    @PutMapping("storeManagement/addItemToStore")
    public ResponseEntity<String> addItemToStore(@RequestParam String token,@RequestParam long storeId,@RequestParam String itemName,@RequestParam String description,@RequestParam double itemPrice,@RequestParam int stockAmount,@RequestParam List<String> categories) {
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

}
