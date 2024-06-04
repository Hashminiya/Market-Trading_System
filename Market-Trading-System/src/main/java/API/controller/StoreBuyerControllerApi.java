package API.controller;

import ServiceLayer.Store.StoreBuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class StoreBuyerControllerApi {

    private final StoreBuyerService storeBuyerService;

    @Autowired
    public StoreBuyerControllerApi(StoreBuyerService storeBuyerService){
        this.storeBuyerService = storeBuyerService;
    }

    @GetMapping("storeBuyer/getAllProductsInfoByStore")
    public ResponseEntity<String> getAllProductsInfoByStore(@RequestParam long storeId) {
        return storeBuyerService.getAllProductsInfoByStore(storeId);
    }

    @GetMapping("storeBuyer/getAllStoreInfo")
    public ResponseEntity<String> getAllStoreInfo() {
        return storeBuyerService.getAllStoreInfo();
    }

    @GetMapping("storeBuyer/searchInStoreByCategory")
    public ResponseEntity<String> searchInStoreByCategory(long storeId, String category) {
        return storeBuyerService.searchInStoreByCategory(storeId, category);
    }

    @GetMapping("storeBuyer/searchInStoreByKeyWord")
    public ResponseEntity<String> searchInStoreByKeyWord(long storeId, String keyWord) {
        return storeBuyerService.searchInStoreByKeyWord(storeId, keyWord);
    }

    @GetMapping("storeBuyer/searchInStoreByKeyWordAndCategory")
    public ResponseEntity<String> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord) {
        return storeBuyerService.searchInStoreByKeyWordAndCategory(storeId, category, keyWord);
    }

    @GetMapping("storeBuyer/searchGenerallyByCategory")
    public ResponseEntity<String> searchGenerallyByCategory(String category) {
        return storeBuyerService.searchGenerallyByCategory(category);
    }

    @GetMapping("storeBuyer/searchGenerallyByKeyWord")
    public ResponseEntity<String> searchGenerallyByKeyWord(String keyWord) {
        return storeBuyerService.searchGenerallyByKeyWord(keyWord);
    }

    @GetMapping("storeBuyer/searchGenerallyByKeyWordAndCategory")
    public ResponseEntity<String> searchGenerallyByKeyWordAndCategory(String category, String keyWord) {
        return storeBuyerService.searchGenerallyByKeyWordAndCategory(category, keyWord);
    }

}
