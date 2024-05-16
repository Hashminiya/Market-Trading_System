package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

import java.util.List;

public class SupplyServiceProxy implements ISupplyService {
    private SupplyServiceImpl supplyService;

    public SupplyServiceProxy(SupplyServiceImpl supplyService) {
        this.supplyService = supplyService;
    }

    @Override
    public boolean validateItemSupply(String storeId,String itemId, int quantity) {
        return supplyService.validateItemSupply(storeId,itemId, quantity);
    }

    @Override
    public boolean performItemSupply(String storeId,,String itemId, int quantity) {
        return supplyService.performItemSupply(storeId,itemId, quantity);
    }

    public boolean validateCartSupply(List<ItemDTO> itemsList) {
        for (ItemDTO item:itemsList) {
            if(!validateItemSupply(item.getStoreId,item.getItemId(),item.getQuantity()))
                return false;
        }
        return  true;
    }
    public void performCartSupply(List<ItemDTO> itemsList) {
        for (ItemDTO item:itemsList ) {
            performItemSupply(item.getStoreId,item.getItemId(),item.getQuantity());
        }
    }
}