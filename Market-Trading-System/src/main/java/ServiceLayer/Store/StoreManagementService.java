package ServiceLayer.Store;

import DomainLayer.Market.Store.IStoreFacade;

import javax.ws.rs.core.Response;
import java.util.List;

public class StoreManagementService implements IStoreManagementService{
    private final IStoreFacade storeFacade;
    public StoreManagementService(IStoreFacade storeFacade){
        this.storeFacade = storeFacade;
    }

    @Override
    public Response createStore(long founderId, String storeName, String storeDescription) {
        try {
            storeFacade.createStore(founderId,storeName,storeDescription);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, List<String> categories) {
        try {
            storeFacade.addItemToStore(storeId,itemName,itemPrice,stockAmount,categories);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return null;
    }

    @Override
    public Response updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int newAmount) {
        try {
            storeFacade.updateItem(userId,storeId,itemId,newName,newPrice, newAmount);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }


    @Override
    public Response deleteItem(long userId, long storeId, long itemId) {
        try {
            storeFacade.deleteItem(userId, storeId, itemId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeStorePolicy(long userId, long storeId) {
        try {
            storeFacade.changeStorePolicy(userId, storeId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeDiscountType(long userId, long storeId, String newType) {
        try {
            storeFacade.changeDiscountType(userId ,storeId, newType);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response removeStore(long userId, long storeId) {
        try {
            storeFacade.removeStore(userId ,storeId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewManagmentInfo(long userId, Long storeId) {
        try {
            return Response.ok().entity(storeFacade.viewStoreManagementInfo(userId, storeId)).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return null;
    }

    @Override
    public Response viewPurchasesHistory(long userId, Long storeId) {
        try {
            return Response.ok().entity(storeFacade.viewPurchaseHistory(userId, storeId)).build();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Response assignStoreOwner(long userId, long storeId, long newOwnerId) {
        try {
            storeFacade.assignStoreOwner(userId, storeId, newOwnerId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response assignStoreManager(long userId, long storeId, long newManagerId) {
        try {
            storeFacade.assignStoreManager(userId, storeId, newManagerId);
            return Response.ok().build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
