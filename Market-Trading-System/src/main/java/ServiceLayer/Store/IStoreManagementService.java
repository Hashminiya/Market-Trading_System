package ServiceLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Util.IRepository;

import javax.ws.rs.core.Response;
import java.util.List;

public interface IStoreManagementService {
    public Response createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Discount> repository);
    public Response addItemToStore(String userId, long storeId, String itemName, String description ,double itemPrice, int stockAmount, List<String> categories);
    public Response updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int newAmount);
    public Response deleteItem(String userId, long storeId, long itemId);
    public Response changeStorePolicy(String userId, long storeId);
    public Response changeDiscountType(String userId, long storeId, String newType);
    public Response removeStore(String userId, long storeId);
    public Response viewManagmentInfo(String userId, Long storeId);
    public Response viewPurchasesHistory(String userId, Long storeId);
    public Response assignStoreOwner(String userId, long storeId, String newOwnerId);
    public Response assignStoreManager(String userId, long storeId ,String newManagerId);
}
