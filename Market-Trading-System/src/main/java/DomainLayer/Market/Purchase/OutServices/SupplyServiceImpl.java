package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.ISupplyService;

public class SupplyServiceImpl implements ISupplyService {
    @Override
    public boolean validateItemSupply(String storeId, String itemId, int quantity) {
        // Logic to check item supply
        if (itemId != null && !itemId.isEmpty() && quantity > 0) {
            System.out.println("Validating supply for item: " + itemId + " with quantity: " + quantity);
            return true;
        }
        return false;
    }

    @Override
    public boolean performItemSupply(String storeId,String itemId, int quantity) {
        //make supply
        return true;
    }
}
