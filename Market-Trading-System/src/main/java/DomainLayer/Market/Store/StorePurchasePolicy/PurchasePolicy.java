package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "policy_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class PurchasePolicy implements DataItem<Long> {
    ///TODO decide how to get relevant user data
    @Id
    private Long id;
    private String name;

    @ElementCollection
    private List<Long> itemsList;

    @ElementCollection
    private List<String> categories;

    private boolean isStore;
    public PurchasePolicy(Long id, String name, List<Long> itemsList, List<String> categories, boolean isStore){
        this.id = id;
        this.name = name;
        this.itemsList = itemsList;
        this.categories = categories;
        this.isStore = isStore;
    }

    public PurchasePolicy() {

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
            return !itemCategories.isEmpty();
        }
        if (itemsList != null) {
            return itemsList.contains(item.getId());
        }
        return false;
    }

    public abstract boolean isValid(HashMap<Item,Integer> itemsInBasket, String userDetails);

    public List<PurchasePolicy> getPolicies() {
        return null;
    }
}
