package ServiceLayer.Store;

import javax.ws.rs.core.Response;


public interface IStoreBuyerService {
    public Response getAllProductsInfoByStore(long storeId);
    public Response getAllStoreInfo(long storeId);
    public Response searchInStoreByCategory(long storeId, String category);
    public Response searchInStoreByKeyWord(long storeId, String keyWord);
    public Response searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord);
    public Response searchGenerallyByCategory(String category);
    public Response searchGenerallyByKeyWord(String keyWord);
    public Response searchGenerallyByKeyWordAndCategory(String category, String keyWord);
}
