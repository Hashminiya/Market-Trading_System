package ServiceLayer.Market;

import javax.ws.rs.core.Response;

public interface ISystemManagerService {
    public Response init(String token);
    public Response viewMarketPurchaseHistory(String token);
    public Response closeStore(String token, long storeId);
    public Response closeMarket(String token);

}
