package ServiceLayer.Store;

import DomainLayer.Market.Store.IStoreFacade;

import java.util.List;

public class StoreManagementService implements IStoreManagementService{
    private final IStoreFacade storeFacade;
    public StoreManagementService(IStoreFacade storeFacade){
        this.storeFacade = storeFacade;
    }

    @Override
    public String createStore(long founderId, String storeName, String storeDescription) {
        try {
            storeFacade.createStore(founderId,storeName,storeDescription);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String addItemToStore(long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain) {
        try {
            storeFacade.addItemToStore(storeId,itemName,itemPrice,stockAmount,categoryChain);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String updateItemName(long storeId, long itemId, String newName) {
        try {
            storeFacade.updateItemName(storeId, itemId, newName);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String updateItemPrice(long storeId, long itemId, double newPrice) {
        try {
            storeFacade.updateItemPrice(storeId, itemId, newPrice);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String decreaseItemAmount(long storeId, long itemId, int count) {
        try {
            storeFacade.decreaseItemAmount(storeId, itemId, count);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String deleteItem(long storeId, long itemId) {
        try {
            storeFacade.deleteItem(storeId, itemId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String changeStorePolicy(long storeId) {
        try {
            storeFacade.changeStorePolicy(storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;    }

    @Override
    public String changeDiscountType(long storeId, String newType) {
        try {
            storeFacade.changeDiscountType(storeId, newType);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String assignOwner(long storeId, Long newOwnerId) {
        try {
            storeFacade.assignStoreOwner(storeId, newOwnerId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String changeManagerPermissions(long storeId, long managerId) {
        try {
            storeFacade.(storeId, newOwnerId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String removeStore(long storeId) {
        try {
            storeFacade.removeStore(storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String viewManagementInfo(Long storeId) {
        return null;
    }

    @Override
    public String viewPurchasesHistory(Long storeId) {
        return null;
    }

    @Override
    public void assignStoreOwner(long userId) {

    }

    @Override
    public void assignStoreManager(long userId) {

    }
}
