package ServiceLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Util.IRepository;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IStoreManagementService {
    public Response createStore(String founderToken, String storeName, String storeDescription, IRepository<Long, Discount> repository);
    public Response addItemToStore(String token, long storeId, String itemName, String description ,double itemPrice, int stockAmount, List<String> categories);
    public Response updateItem(String token, long storeId, long itemId, String newName, double newPrice, int newAmount);
    public Response deleteItem(String token, long storeId, long itemId);
    public Response changeStorePolicy(String token, long storeId);
    public Response changeDiscountType(String token, long storeId, String newType);
    public Response removeStore(String token, long storeId);
    public Response viewManagementInfo(String token, long storeId);
    public Response viewInventory(String token, long storeId);
    public Response viewPurchasesHistory(String token, long storeId);
    public Response assignStoreOwner(String token, long storeId, String newOwnerId);
    public Response assignStoreManager(String token, long storeId ,String newManagerId, List<String> permissions);
}
