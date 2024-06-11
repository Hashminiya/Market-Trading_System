package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaximumQuantityPurchasePolicy extends PurchsePoilcy{
    private final int maxAmount;
    @JsonCreator
    public MaximumQuantityPurchasePolicy(@JsonProperty("name") String name,
                                         @JsonProperty("id") Long id,
                                         @JsonProperty("minAge") int maxAmount,
                                         @JsonProperty("items") IRepository<Long, Item> items,
                                         @JsonProperty("categories") List<String> categories,
                                         @JsonProperty("isStore") boolean isStore){
        super(id, name, items, categories, isStore);
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
