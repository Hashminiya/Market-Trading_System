package ServiceLayer.Store;

import java.util.List;

public interface IStoreManagementService {
    public String createStore(long founderId, String storeName, String storeDescription);
    public String addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain);
    public String updateItemName(long userId, long storeId, long itemId, String newName);
    public String updateItemPrice(long userId, long storeId, long  itemId, double newPrice);
    public String decreaseItemAmount(long userId, long storeId, long itemId, int count);
    public String deleteItem(long userId, long storeId, long itemId);
    public String changeStorePolicy(long userId, long storeId);
    public String changeDiscountType(long userId, long storeId, String newType);
    public String changeManagerPermissions(long userId, long storeId, long managerId);
    public String removeStore(long userId, long storeId);
    public String viewManagmentInfo(long userId, Long storeId);
    public String viewPurchasesHistory(long userId, Long storeId);
    public String assignStoreOwner(long userId, long storeId, long newOwnerId);
    public String assignStoreManager(long userId, long storeId ,long newManagerId);
}
