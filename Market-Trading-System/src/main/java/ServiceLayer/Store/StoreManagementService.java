package ServiceLayer.Store;

import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;

import javax.ws.rs.core.Response;
import java.util.List;

public class StoreManagementService implements IStoreManagementService{
    private static StoreManagementService instance;
    private final IStoreFacade storeFacade;

    private StoreManagementService(IStoreFacade storeFacade){
        this.storeFacade = storeFacade;
    }

    // Public method to provide access to the instance
    public static StoreManagementService getInstance(IStoreFacade storeFacade) {
        if (instance == null) {
            instance = new StoreManagementService(storeFacade);
        }
        return instance;
    }

    @Override
    public Response createStore(String founderId, String storeName, String storeDescription, IRepository<Long,Discount> repository) {
        try {
            storeFacade.createStore(founderId,storeName,storeDescription,repository);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response addItemToStore(String userId, long storeId, String itemName, String description ,double itemPrice, int stockAmount, List<String> categories) {
        try {
            storeFacade.addItemToStore(userId,storeId,itemName,itemPrice,stockAmount, description,categories);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int newAmount) {
        try {
            storeFacade.updateItem(userId,storeId,itemId,newName,newPrice, newAmount);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }


    @Override
    public Response deleteItem(String userId, long storeId, long itemId) {
        try {
            storeFacade.deleteItem(userId, storeId, itemId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeStorePolicy(String userId, long storeId) {
        try {
            storeFacade.changeStorePolicy(userId, storeId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeDiscountType(String userId, long storeId, String newType) {
        try {
            storeFacade.changeDiscountType(userId ,storeId, newType);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response removeStore(String userId, long storeId) {
        try {
            storeFacade.removeStore(userId ,storeId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewManagmentInfo(String userId, Long storeId) {
        try {
            return Response.ok().entity(storeFacade.viewStoreManagementInfo(userId, storeId)).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewPurchasesHistory(String userId, Long storeId) {
        try {
            return Response.ok().entity(storeFacade.viewPurchaseHistory(userId, storeId)).build();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Response assignStoreOwner(String userId, long storeId, String newOwnerId) {
        try {
            storeFacade.assignStoreOwner(userId, storeId, newOwnerId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response assignStoreManager(String userId, long storeId, String newManagerId) {
        try {
            storeFacade.assignStoreManager(userId, storeId, newManagerId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
