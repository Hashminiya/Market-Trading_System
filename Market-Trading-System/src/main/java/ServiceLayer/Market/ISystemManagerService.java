package ServiceLayer.Market;

import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

public interface ISystemManagerService {
    public ResponseEntity<?> init(String token);
    public ResponseEntity<?> viewMarketPurchaseHistory(String token);
    public ResponseEntity<?> closeStore(String token, long storeId);
    public ResponseEntity<?> closeMarket(String token);

}
