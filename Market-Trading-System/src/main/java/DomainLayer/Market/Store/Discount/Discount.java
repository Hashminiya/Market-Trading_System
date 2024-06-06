package DomainLayer.Market.Store.Discount;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class Discount implements IDiscount {

    protected Long id;
    protected double percent;
    protected Date expirationDate;
    protected Long storeId;
    protected List<Long> items;
    protected List<String> categories;

    public Discount(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
        this.items = items;
        this.categories = categories;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public abstract boolean isValid(Map<Long, Integer> items, String code);

    public void setItems(List<Long> items){ this.items = items; }

    public boolean isByCategory(){ return !(this.categories == null || this.categories.isEmpty()); }

    public List<String> getCategories(){ return categories;}

    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices) {
        if(itemsPrices == null)
            throw new NullPointerException("Null items prices List");
        for(Long itemId: itemsPrices.keySet().stream().toList()){
            if(items.contains(itemId)){
                double originalPrice = itemsPrices.get(itemId);
                itemsPrices.replace(itemId, originalPrice, (1-percent/100) * originalPrice);
            }
        }
        return itemsPrices;
    }

}
