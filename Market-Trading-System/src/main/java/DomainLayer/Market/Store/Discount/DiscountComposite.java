package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public abstract class DiscountComposite extends BaseDiscount {

    /*protected double percent;
    protected Date expirationDate;
    protected Long storeId;*/
    /*protected List<Long> items;
    protected List<String> categories;
    protected boolean isStore;*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_discount_id")
    protected List<BaseDiscount> discounts;


    public DiscountComposite(Long id, List<BaseDiscount> discounts, String name){
        super(id,name);
        this.discounts = discounts;
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