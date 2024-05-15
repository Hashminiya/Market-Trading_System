package DomainLayer.Market.Purchase;

import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

public class SupplyServiceProxy implements ISupplyService {
    private SupplyServiceImpl supplyService;

    public SupplyServiceProxy(SupplyServiceImpl supplyService) {
        this.supplyService = supplyService;
    }

    @Override
    public boolean validateItemSupply(String itemId, int quantity) {
        return supplyService.validateItemSupply(itemId, quantity);
    }
}