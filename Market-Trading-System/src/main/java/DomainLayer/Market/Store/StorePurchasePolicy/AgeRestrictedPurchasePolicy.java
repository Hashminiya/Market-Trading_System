package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Discount.ICondition;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgeRestrictedPurchasePolicy extends PurchsePoilcy {
    private final int minAge;
    IUserFacade userFacade;
    @JsonCreator
    public AgeRestrictedPurchasePolicy(@JsonProperty("name") String name,
                                       @JsonProperty("id") Long id,
                                       @JsonProperty("minAge") int minAge,
                                       @JsonProperty("items") IRepository<Long, Item> items,
                                       @JsonProperty("categories") List<String> categories,
                                       @JsonProperty("isStore") boolean isStore,
                                       IUserFacade userFacade){
        super(id, name, items, categories, isStore);
        this.minAge = minAge;
        this.userFacade = userFacade;

    }
    @Override
    public boolean isValid(HashMap<Item,Integer> itemsInBasket, String userName) {
        if(userFacade.getUserAge(userName) < minAge) {
            for (Item itemToCheck : itemsInBasket.keySet()) {
                if (isIncluded(itemToCheck))
                    return false;
            }
        }
        return true;
    }

}
