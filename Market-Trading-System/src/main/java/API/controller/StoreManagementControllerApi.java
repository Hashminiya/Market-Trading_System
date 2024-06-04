package API.controller;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Util.IRepository;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.Store.StoreManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.List;

@RestController
public class StoreManagementControllerApi {

    private final StoreManagementService storeManagementService;

    @Autowired
    public StoreManagementControllerApi(StoreManagementService storeManagementService){
        this.storeManagementService = storeManagementService;
    }

    @GetMapping("storeManagement/createStore")
    public ResponseEntity<String> createStore(@RequestParam String founderToken, @RequestParam String storeName, @RequestParam String storeDescription,@RequestParam IRepository<Long, Discount> repository) {
        return storeManagementService.createStore(founderToken, storeName, storeDescription, repository);
    }

    @GetMapping("storeManagement/addItemToStore")
    public ResponseEntity<String> addItemToStore(@RequestParam String token,@RequestParam long storeId,@RequestParam String itemName,@RequestParam String description,@RequestParam double itemPrice,@RequestParam int stockAmount,@RequestParam List<String> categories) {
        return storeManagementService.addItemToStore(token, storeId, itemName, description, itemPrice, stockAmount, categories);
    }

    @GetMapping("storeManagement/updateItem")
    public ResponseEntity<String> updateItem(@RequestParam String token,@RequestParam long storeId,@RequestParam long itemId,@RequestParam String newName,@RequestParam double newPrice,@RequestParam int newAmount) {
        return storeManagementService.updateItem(token, storeId, itemId, newName, newPrice, newAmount);
    }

    @GetMapping("storeManagement/deleteItem")
    public ResponseEntity<String> deleteItem(@RequestParam String token,@RequestParam long storeId,@RequestParam long itemId) {
        return storeManagementService.deleteItem(token, storeId, itemId);
    }

    @GetMapping("storeManagement/changeStorePolicy")
    public ResponseEntity<String> changeStorePolicy(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.changeStorePolicy(token, storeId);
    }

    @GetMapping("storeManagement/changeDiscountType")
    public ResponseEntity<String> changeDiscountType(@RequestParam String token,@RequestParam long storeId,@RequestParam String newType) {
        return storeManagementService.changeDiscountType(token, storeId, newType);
    }

    @GetMapping("storeManagement/removeStore")
    public ResponseEntity<String> removeStore(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.removeStore(token, storeId);
    }

    @GetMapping("storeManagement/viewManagementInfo")
    public ResponseEntity<String> viewManagementInfo(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.viewManagementInfo(token, storeId);
    }

    @GetMapping("storeManagement/viewInventory")
    public ResponseEntity<String> viewInventory(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.viewInventory(token, storeId);
    }

    @GetMapping("storeManagement/viewPurchasesHistory")
    public ResponseEntity<String> viewPurchasesHistory(@RequestParam String token,@RequestParam long storeId) {
        return storeManagementService.viewPurchasesHistory(token, storeId);
    }

    @GetMapping("storeManagement/assignStoreOwner")
    public ResponseEntity<String> assignStoreOwner(@RequestParam String token,@RequestParam long storeId,@RequestParam String newOwnerId) {
        return storeManagementService.assignStoreOwner(token, storeId, newOwnerId);
    }

    @GetMapping("storeManagement/assignStoreManager")
    public ResponseEntity<String> assignStoreManager(@RequestParam String token,@RequestParam long storeId,@RequestParam String newManagerI,@RequestParam List<String> permissions) {
        return storeManagementService.assignStoreManager(token, storeId, newManagerI, permissions);
    }

}