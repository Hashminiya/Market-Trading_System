package ServiceLayer.Store;

import javax.ws.rs.core.Response;
import DomainLayer.Market.Store.IStoreFacade;

import java.util.HashMap;

public class StoreBuyerService implements IStoreBuyerService{

    private IStoreFacade storeFacade;
    private final String EMPTY_RESULT_ERROR = "Error: 0 results for search";

    public StoreBuyerService(IStoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    @Override
    public Response getAllProductsInfoByStore(long storeId) {
        try{
            HashMap<Long, HashMap<String, String>> result = storeFacade.getAllProductsInfoByStore(storeId);
            return Response.ok(result).build();
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response getAllStoreInfo(long storeId) {
        try{
            HashMap<Long, HashMap<String, String>> result = storeFacade.getAllStoreInfo(storeId);
            return Response.ok(result).build();
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response searchStoreByCategory(long category) {
        try{
            HashMap<Long, String> result = storeFacade.searchStoreByCategory(category);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();

    }

    @Override
    public Response searchItemByCategory(long category) {
        try{
            HashMap<Long, String> result = storeFacade.searchItemByCategory(category);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();
    }

    @Override
    public Response searchStoreByKeyWord(String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchStoreByKeyWord(keyWord);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();
    }

    @Override
    public Response searchItemByKeyWord(String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchItemByKeyWord(keyWord);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();
    }

    @Override
    public Response searchStoreByName(String name) {
        try{
            HashMap<Long, String> result = storeFacade.searchStoreByName(name);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();
    }

    @Override
    public Response searchItemByName(String name) {
        try{
            HashMap<Long, String> result = storeFacade.searchItemByName(name);
            if(!result.isEmpty()) {
                return Response.ok(result).build();
            }
        }
        catch(Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
        return Response.status(204).entity(EMPTY_RESULT_ERROR).build();
    }

}
