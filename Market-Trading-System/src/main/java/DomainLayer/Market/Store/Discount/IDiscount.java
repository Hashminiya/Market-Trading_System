package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.Map;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface IDiscount extends DataItem<Long> {

    public Long getId();

    public String getName();

    public Map<Item, Double> getPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code)throws Exception;

    public boolean isValid(Map<Item, Integer> items, String code);

    public Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception;

}
