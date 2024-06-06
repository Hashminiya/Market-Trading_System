package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.DataItem;

import java.util.Date;
import java.util.Map;
import java.util.List;


public interface IDiscount extends DataItem<Long> {

    public Long getId();

    public String getName();

    public Date getExpirationDate();

    public List<String> getCategories();

    public void setItems(List<Long> items);

    public boolean isByCategory();

    public boolean isValid(Map<Long, Integer> items, String code);

    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices);

}
