package DomainLayer.Market.Store;

import java.util.List;

public interface IStoreFacade {
    public void createStore(long founderId, String storeName, String storeDescription);
    public void addItemToStore(long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain);
    public void updateItemName(long storeId, long itemId, String newName);
    public void updateItemPrice(long storeId, long  itemId, double newPrice);
    public void decreaseItemAmount(long storeId, long itemId, int count);
    public void changeStorePolicy(long storeId);
    public void changeDiscountType(long storeId, String newType);
    public void removeStore(long storeId);

}
