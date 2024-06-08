package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Util.DataItem;

import java.util.HashMap;

public abstract class PurchsePoilcy implements DataItem<Long> {
    ///TODO decide how to get relevant user data
    private final Long id;
    private final String name;
    public PurchsePoilcy(Long id, String name){
        this.id = id;
        this.name = name;
    }
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public abstract boolean isValid(HashMap<Long,Integer> itemsInBasket, String userDetails);

}
