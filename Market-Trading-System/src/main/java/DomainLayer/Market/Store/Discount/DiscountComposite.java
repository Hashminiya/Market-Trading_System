package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.IRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class DiscountComposite implements IDiscount {

    protected Long id;
    /*protected double percent;
    protected Date expirationDate;
    protected Long storeId;*/
    /*protected List<Long> items;
    protected List<String> categories;
    protected boolean isStore;*/
    protected List<IDiscount> discounts;


    public DiscountComposite(Long id, List<IDiscount> discounts){
        this.id = id;
        this.discounts = discounts;
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
    public boolean isValid(Map<Item, Integer> items, String code) {
        for(IDiscount discount: discounts){
            if(!discount.isValid(items, code))
                return false;
        }
        return true;
    }

    public abstract Map<Item, Double> getPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception;

    public abstract Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception;

}
