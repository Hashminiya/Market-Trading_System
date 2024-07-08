package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SupplyServiceProxy")
public class SupplyServiceProxy implements ISupplyService {
    private static SupplyServiceProxy instance;
    private SupplyServiceImpl supplyService;

    @Autowired
    private SupplyServiceProxy(SupplyServiceImpl supplyService) {
        this.supplyService = supplyService;
    }

    public static synchronized SupplyServiceProxy getInstance(SupplyServiceImpl supplyServiceInstance) {
        if(instance == null) {
            instance = new SupplyServiceProxy(supplyServiceInstance);
        }
        return instance;
    }

    @Override
    public int performCartSupply() throws Exception {
        return supplyService.performCartSupply();
    }

    @Override
    public int cancelCartSupply(int transactionId) throws Exception {
        return supplyService.cancelCartSupply(transactionId);
    }

}