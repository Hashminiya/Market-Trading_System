package DomainLayer.Market.Purchase.Abstractions;

public interface ISupplyService {
    boolean validateItemSupply(long storeId,long itemId, int quantity);
    boolean performItemSupply(long storeId,long itemId, int quantity);
}
