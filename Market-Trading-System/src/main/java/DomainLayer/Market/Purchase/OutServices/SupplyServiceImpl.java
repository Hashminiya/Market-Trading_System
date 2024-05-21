package DomainLayer.Market.Purchase.OutServices;

import DomainLayer.Market.Purchase.Abstractions.ISupplyService;
import DomainLayer.Market.Purchase.SupplyServiceProxy;

public class SupplyServiceImpl implements ISupplyService {
    private static SupplyServiceImpl instance;

    private SupplyServiceImpl() {}

    public static SupplyServiceImpl getInstance() {
        if(instance == null) {
            instance = new SupplyServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean validateItemSupply(long storeId, long itemId, int quantity) {
        // Logic to check item supply
        if (quantity < 0) {
            throw new RuntimeException("Quantity cant be a negative number");
        }
        return true;
    }

    @Override
    public boolean performItemSupply(long storeId,long itemId, int quantity) {
        //make supply
        return true;
    }
}
