package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Discount implements IDiscount {

    protected Long id;
    protected double percent;
    protected Date expirationDate;
    protected Long storeId;
    protected List<Long> items;
    protected List<String> categories;
    protected boolean isStore;

    public Discount(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, boolean isStore){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
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
        return "";
    }

    public abstract boolean isValid(Map<Item, Integer> items, String code);

    public Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) {
        Map<Item, Double> newItemsPrices = new HashMap<>(itemsPrices);
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
                    newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent/100) * item.getPrice());
                }
            }
            else if(isStore)
                newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent/100) * newItemsPrices.get(item));
            else
                if(items.contains(item.getId()))
                    newItemsPrices.replace(item, newItemsPrices.get(item),(1-percent/100) * newItemsPrices.get(item));

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
