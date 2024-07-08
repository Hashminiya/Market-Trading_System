package DomainLayer.Market.Purchase.Abstractions;

public interface ISupplyService {
    int performCartSupply() throws Exception;
    int cancelCartSupply(int transactionId) throws Exception;
}
