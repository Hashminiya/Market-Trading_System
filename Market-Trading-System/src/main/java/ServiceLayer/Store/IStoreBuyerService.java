package ServiceLayer.Store;

import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.util.HashMap;


public interface IStoreBuyerService {
    public ResponseEntity<?> getAllProductsInfoByStore(long storeId);
    public ResponseEntity<?> getAllStoreInfo();
    public ResponseEntity<?> searchInStoreByCategory(long storeId, String category);
    public ResponseEntity<?> searchInStoreByKeyWord(long storeId, String keyWord);
    public ResponseEntity<?> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord);
    public ResponseEntity<?> searchGenerallyByCategory(String category);
    public ResponseEntity<?> searchGenerallyByKeyWord(String keyWord);
    public ResponseEntity<?> searchGenerallyByKeyWordAndCategory(String category, String keyWord);
}
