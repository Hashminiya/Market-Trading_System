package ServiceLayer.Store;

public interface IStoreManagementService {
    String createStore();
    String viewInventory();
    String modifyItem(Long storeId, Long itemId);
    String deleteItem(Long storeId, Long itemId);
    String changePolicy(Long storeId);
    String assignOwner(Long storeId, Long newOwnerId);
    String changeManagerPermissions(Long storeId, Long managerId);
    String removeStore(Long storeId);
    String viewManagementInfo(Long storeId);
    String viewPurchasesHistory(Long storeId);
}
