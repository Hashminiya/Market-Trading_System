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
    public String addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain) {
        try {
            storeFacade.addItemToStore(storeId,itemName,itemPrice,stockAmount,categoryChain);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String updateItemName(long userId, long storeId, long itemId, String newName) {
        try {
            storeFacade.updateItemName(storeId, itemId, newName);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String updateItemPrice(long userId, long storeId, long itemId, double newPrice) {
        try {
            storeFacade.updateItemPrice(storeId, itemId, newPrice);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String decreaseItemAmount(long userId, long storeId, long itemId, int count) {
        try {
            storeFacade.decreaseItemAmount(storeId, itemId, count);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String deleteItem(long userId, long storeId, long itemId) {
        try {
            storeFacade.deleteItem(storeId, itemId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String changeStorePolicy(long userId, long storeId) {
        try {
            storeFacade.changeStorePolicy(storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;    }

    @Override
    public String changeDiscountType(long userId, long storeId, String newType) {
        try {
            storeFacade.changeDiscountType(storeId, newType);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String changeManagerPermissions(long userId, long storeId, long managerId) {
        ///TODO what to send as 'new_permissions'?
        try {
            storeFacade.changePermission(storeId, newOwnerId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String removeStore(long userId, long storeId) {
        try {
            storeFacade.removeStore(storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String viewManagmentInfo(long userId, Long storeId) {
        try {
            storeFacade.viewManagementInfo(userId, storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String viewPurchasesHistory(long userId, Long storeId) {
        try {
            storeFacade.viewPurchaseHistory(userId, storeId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String assignStoreOwner(long userId, long storeId, long newOwnerId) {
        try {
            storeFacade.assignStoreOwner(userId, storeId, newOwnerId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public String assignStoreManager(long userId, long storeId, long newManagerId) {
        try {
            storeFacade.assignStoreManager(userId, storeId, newManagerId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
