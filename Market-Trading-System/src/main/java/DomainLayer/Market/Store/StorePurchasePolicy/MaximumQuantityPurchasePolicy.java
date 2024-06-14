package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class MaximumQuantityPurchasePolicy extends PurchasePolicy {
    private final int maxAmount;
    @JsonCreator
    public MaximumQuantityPurchasePolicy(@JsonProperty("name") String name,
                                         @JsonProperty("id") Long id,
                                         @JsonProperty("maxAmount") int maxAmount,
                                         @JsonProperty("items") List<Long> items,
                                         @JsonProperty("categories") List<String> categories,
                                         @JsonProperty("isStore") boolean isStore){
        super(id, name, items,categories, isStore);
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean isValid(HashMap<Item, Integer> itemsInBasket, String userDetails) {
        for (Map.Entry<Item, Integer> itemToAmount: itemsInBasket.entrySet()
             ) {
            if(itemToAmount.getValue() > maxAmount && isIncluded(itemToAmount.getKey()))
                return false;
        }
        return true;
    }
}
