package DomainLayer.Market.Store.Discount;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class DiscountComposite implements IDiscount {

    protected Long id;
    protected double percent;
    protected Date expirationDate;
    protected Long storeId;
    protected List<Long> items;
    protected List<String> categories;
    protected List<IDiscount> discounts;

    public DiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, List<IDiscount> discounts){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
        this.discounts = discounts;
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

    @Override
    public Date getExpirationDate(){
        return expirationDate;
    }

    @Override
    public List<String> getCategories(){ return categories;}

    @Override
    public void setItems(List<Long> items) {this.items = items; }

    @Override
    public boolean isByCategory(){ return !(this.categories == null || this.categories.isEmpty()); }

    public abstract boolean isValid(Map<Long, Integer> items, String code);

    public abstract Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices);

}
