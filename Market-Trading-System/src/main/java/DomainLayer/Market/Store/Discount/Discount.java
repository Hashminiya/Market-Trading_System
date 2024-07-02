package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("Discount")
@NoArgsConstructor
public abstract class Discount extends BaseDiscount {
    protected boolean isStore;
    protected double percent;
    protected Date expirationDate;
    protected Long store_id;

    @ElementCollection
    protected List<Long> items;

    @ElementCollection
    protected List<String> categories;

    public Discount(Long id, double percent, Date expirationDate, long store_id, List<Long> items, List<String> categories, boolean isStore){
        super(id);
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.store_id = store_id;
        this.items = items;
        this.categories = categories;
        this.isStore = isStore;
    }


    public abstract boolean isValid(Map<Item, Integer> items, String code);

    public Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) {
        Map<Item, Double> newItemsPrices = new HashMap<>(itemsPrices);
        for(Item item: itemsCount.keySet().stream().toList()){
            if(categories != null && !categories.isEmpty()){
                boolean found = false;
                for(String category: categories){
                    if(item.getCategories().contains(category)){
                        found = true;
                        break;
                    }
                }
                if(found){
                    newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent) * item.getPrice());
                }
            }
            else if(isStore)
                newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent) * newItemsPrices.get(item));
            else
                if(items.contains(item.getId()))
                    newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent) * newItemsPrices.get(item));

        }
        return newItemsPrices;
    }

    @Override
    public Map<Item, Double> getPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception{
        Map<Item, Double> itemsPercent = new HashMap<>();
        for(Item item: itemsCount.keySet().stream().toList()){
            if(!categories.isEmpty()){
                boolean found = false;
                for(String category: categories){
                    if(item.getCategories().contains(category)){
                        found = true;
                        break;
                    }
                }
                if(found){
                    itemsPercent.put(item, percent);
                }
                else{
                    itemsPercent.put(item, 0.0);
                }
            }
            else if(isStore)
                itemsPercent.put(item, percent);
            else
            if(items.contains(item.getId()))
                itemsPercent.put(item, percent);
            else
                itemsPercent.put(item, 0.0);
        }
        return itemsPercent;
    }
}
