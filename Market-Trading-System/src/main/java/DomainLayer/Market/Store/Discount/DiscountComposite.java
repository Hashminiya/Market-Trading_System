package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.IRepository;

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
    protected boolean isStore;
    protected List<IDiscount> discounts;


    public DiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, boolean isStore, List<IDiscount> discounts){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
        this.discounts = discounts;
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

    @Override
    public boolean isValid(Map<Long, Integer> items, String code) {
        Date now = new Date();
        if(!getExpirationDate().after(now))
            return false;
        for(IDiscount discount: discounts){
            if(!discount.isValid(items, code))
                return false;
        }
        return true;
    }

    public abstract Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code);

}
