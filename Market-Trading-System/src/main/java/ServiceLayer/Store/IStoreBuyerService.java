package ServiceLayer.Store;

import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.util.HashMap;


public interface IStoreBuyerService {
    public ResponseEntity<String> getAllProductsInfoByStore(long storeId);
    public ResponseEntity<HashMap<Long, String>> getAllStoreInfo();
    public ResponseEntity<HashMap<Long, String>> searchInStoreByCategory(long storeId, String category);
    public ResponseEntity<HashMap<Long, String>> searchInStoreByKeyWord(long storeId, String keyWord);
    public ResponseEntity<HashMap<Long, String>> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord);
    public ResponseEntity<HashMap<Long, String>> searchGenerallyByCategory(String category);
    public ResponseEntity<HashMap<Long, String>> searchGenerallyByKeyWord(String keyWord);
    public ResponseEntity<HashMap<Long, String>> searchGenerallyByKeyWordAndCategory(String category, String keyWord);
}
