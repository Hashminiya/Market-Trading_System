package DomainLayer.Market.Store.StorePurchasePolicy;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AgeRestrictedPurchasePolicy extends PurchsePoilcy {
    private final int minAge;
    private final List<Long> items;
    Long id;
    String name;


    public AgeRestrictedPurchasePolicy(String name, Long id,int minAge, List<Long> items){
        super(id, name);
        this.name = name;
        this.id = id;
        this.minAge = minAge;
        this.items = items;

    }
    @Override
    public boolean isValid(HashMap<Long,Integer> itemsInBasket, String userDetails) {
        return minAge <= Integer.parseInt(userDetails) ||
                items.stream().filter(itemsInBasket::containsKey)
                .toList().size() == 0;
    }

}
