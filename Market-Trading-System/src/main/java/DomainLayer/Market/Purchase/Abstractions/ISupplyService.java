package DomainLayer.Market.Purchase.Abstractions;

public interface ISupplyService {
    boolean validateItemSupply(String storeId,String itemId, int quantity);
    boolean performItemSupply(String storeId,String itemId, int quantity);
}
