package DomainLayer.Market.Store.StorePurchasePolicy;

import java.util.HashMap;
import java.util.List;

public class MaximumQuantityPurchasePolicy extends PurchsePoilcy{
    private final int maxAmount;
    private final List<Long> items;
    public MaximumQuantityPurchasePolicy(String name, Long id,List<Long> items, int maxAmount){
        super(id, name);
        this.items = items;
        this.maxAmount = maxAmount;
    }
    @Override
    public boolean isValid(HashMap<Long,Integer> itemsInBasket, String userDetails) {
        return
        items.stream().filter(
                id -> itemsInBasket.containsKey(id) && itemsInBasket.get(id) > maxAmount
                ).toList().size() == 0;
    }

}
