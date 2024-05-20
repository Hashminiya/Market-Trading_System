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
    public Response searchInStoreByCategory(long storeId, String category) {
        try{
            HashMap<Long, String> result = storeFacade.searchInStoreByCategory(storeId, category);
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
    public Response searchInStoreByKeyWord(long storeId, String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchInStoreByKeyWord(storeId, keyWord);
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
    public Response searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchInStoreByKeyWordAndCategory(storeId, category, keyWord);
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
    public Response searchGenerallyByCategory(String category) {
        try{
            HashMap<Long, String> result = storeFacade.searchGenerallyByCategory(category);
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
    public Response searchGenerallyByKeyWord(String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchGenerallyByKeyWord(keyWord);
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
    public Response searchGenerallyByKeyWordAndCategory(String category, String keyWord) {
        try{
            HashMap<Long, String> result = storeFacade.searchGenerallyByKeyWordAndCategory(category, keyWord);
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
