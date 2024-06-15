package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class PurchasePolicy implements DataItem<Long> {
    ///TODO decide how to get relevant user data
    private final Long id;
    private final String name;
    private List<Long> itemsList;
    private final List<String> categories;
    private final boolean isStore;
    public PurchasePolicy(Long id, String name, List<Long> itemsList, List<String> categories, boolean isStore){
        this.id = id;
        this.name = name;
        this.itemsList = itemsList;
        this.categories = categories;
        this.isStore = isStore;
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Long> getItemsList() {
        return itemsList;
    }

    public boolean isIncluded(Item item){
        if (isStore) return true;
        if(categories != null) {
            List<String> itemCategories = new ArrayList<>(item.getCategories());
            itemCategories.retainAll(categories);
            return itemCategories.size() > 0;
        }
        else if (itemsList != null) {
            return itemsList.contains(item.getId());
        }
        return false;
    }

    public abstract boolean isValid(HashMap<Item,Integer> itemsInBasket, String userDetails);

    public List<PurchasePolicy> getPolicies() {
        return null;
    }
}
