package DomainLayer.Market.Purchase.Abstractions;

public interface ISupplyService {
    boolean validateItemSupply(String itemId, int quantity);
}
