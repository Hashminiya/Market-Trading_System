package API.controller;

import ServiceLayer.Market.ISystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemManagerControllerApi {

    private final ISystemManagerService systemManagerService;

    @Autowired
    public SystemManagerControllerApi(@Qualifier("SystemManagerService") ISystemManagerService systemManagerService) {
        this.systemManagerService = systemManagerService;
    }

    @GetMapping("/systemManager/init")
    public ResponseEntity<?> init(@RequestParam String token){
        return systemManagerService.init(token);
    }

    @GetMapping("/systemManager/viewMarketPurchaseHistory")
    public ResponseEntity<?> viewMarketPurchaseHistory(@RequestParam String token){
        return systemManagerService.viewMarketPurchaseHistory(token);
    }

    @GetMapping("/systemManager/closeStore")
    public ResponseEntity<?> closeStore(@RequestParam String token,@RequestParam long storeId){
        return systemManagerService.closeStore(token,storeId);
    }

    @GetMapping("/systemManager/closeMarket")
    public ResponseEntity<?> closeMarket(@RequestParam String token){
        return systemManagerService.closeMarket(token);
    }
}
