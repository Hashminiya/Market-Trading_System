package ServiceLayer.Store;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Util.IRepository;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

public interface IStoreManagementService {
    public ResponseEntity<String> createStore(String founderToken, String storeName, String storeDescription, IRepository<Long, Discount> repository);
    public ResponseEntity<String> addItemToStore(String token, long storeId, String itemName, String description ,double itemPrice, int stockAmount, List<String> categories);
    public ResponseEntity<String> updateItem(String token, long storeId, long itemId, String newName, double newPrice, int newAmount);
    public ResponseEntity<String> deleteItem(String token, long storeId, long itemId);
    public ResponseEntity<String> changeStorePolicy(String token, long storeId);
    public ResponseEntity<String> changeDiscountType(String token, long storeId, String newType);
    public ResponseEntity<String> removeStore(String token, long storeId);
    public ResponseEntity<?> viewManagementInfo(String token, long storeId);
    public ResponseEntity<?> viewInventory(String token, long storeId);
    public ResponseEntity<?> viewPurchasesHistory(String token, long storeId);
    public ResponseEntity<String> assignStoreOwner(String token, long storeId, String newOwnerId);
    public ResponseEntity<String> assignStoreManager(String token, long storeId ,String newManagerId, List<String> permissions);
}
