package ServiceLayer.Store;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IStoreManagementService {
    public Response createStore(long founderId, String storeName, String storeDescription);
    public Response addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain);
    public Response updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int newAmount);
    public Response deleteItem(long userId, long storeId, long itemId);
    public Response changeStorePolicy(long userId, long storeId);
    public Response changeDiscountType(long userId, long storeId, String newType);
    public Response removeStore(long userId, long storeId);
    public Response viewManagmentInfo(long userId, Long storeId);
    public Response viewPurchasesHistory(long userId, Long storeId);
    public Response assignStoreOwner(long userId, long storeId, long newOwnerId);
    public Response assignStoreManager(long userId, long storeId ,long newManagerId);
    public Response viewInventory(long userId, long storeId);
}
