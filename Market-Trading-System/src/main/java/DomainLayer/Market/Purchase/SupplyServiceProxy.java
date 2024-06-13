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
    public boolean validateItemSupply(long storeId,long itemId, int quantity) {
        return supplyService.validateItemSupply(storeId,itemId, quantity);
    }

    @Override
    public boolean performItemSupply(long storeId,long itemId, int quantity) {
        return supplyService.performItemSupply(storeId,itemId, quantity);
    }

    public boolean validateCartSupply(List<ItemDTO> itemsList) {
        for (ItemDTO item:itemsList) {
            if(!validateItemSupply(item.getStoreId(),item.getItemId(),item.getQuantity()))
                return false;
        }
        return  true;
    }
    public void performCartSupply(List<ItemDTO> itemsList) {
        for (ItemDTO item:itemsList ) {
            performItemSupply(item.getStoreId(),item.getItemId(),item.getQuantity());
        }
    }
}