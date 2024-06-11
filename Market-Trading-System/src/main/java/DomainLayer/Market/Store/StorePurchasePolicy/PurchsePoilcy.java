package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PurchsePoilcy implements DataItem<Long> {
    ///TODO decide how to get relevant user data
    private final Long id;
    private final String name;
    private final IRepository<Long, Item> items;
    private final List<String> categories;
    private final boolean isStore;
    public PurchsePoilcy(Long id, String name, IRepository<Long, Item> items,
                         List<String> categories,boolean isStore){
        this.id = id;
        this.name = name;
        this.items = items;
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
    public boolean isIncluded(Item item){
        if (isStore) return true;
        if(categories != null) {
            List<String> itemCategories = new ArrayList<>(item.getCategories());
            itemCategories.retainAll(categories);
            return itemCategories.size() > 0;
        }
        else {
            return items.findById(item.getId()) != null;
        }
    }

    public abstract boolean isValid(HashMap<Item,Integer> itemsInBasket, String userDetails);

}
