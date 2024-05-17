package ServiceLayer.Store;

import javax.ws.rs.core.Response;


public interface IStoreBuyerService {
    public Response getAllProductsInfoByStore(long storeId);
    public Response getAllStoreInfo(long storeId);
    public Response searchStoreByCategory(long category);
    public Response searchItemByCategory(long category);
    public Response searchStoreByKeyWord(String keyWord);
    public Response searchItemByKeyWord(String keyWord);
    public Response searchStoreByName(String name);
    public Response searchItemByName(String name);
}
