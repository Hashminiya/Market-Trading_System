package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.NumericRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class NumericDiscountComposite extends DiscountComposite{

    private NumericRule numericRule;


    @JsonCreator
    public NumericDiscountComposite(@JsonProperty("id") Long id,
                                    @JsonProperty("discounts") List<IDiscount> discounts,
                                    @JsonProperty("numericRule") String rule) {
        super(id, discounts);
        this.numericRule = NumericRule.valueOf(rule);
    }


    @Override
    public Map<Item, Double> getPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception {
        return switch (numericRule){
            case MAX-> maxGetPercent(itemsPrices, itemCount, code);
            case ADD ->addGetPercent(itemsPrices, itemCount, code);
            default -> null;
        };
    }

    private Map<Item, Double> maxGetPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception {
        double maxPrice = 0;
        IDiscount maxDiscount = null;
        for(IDiscount discount: discounts){
            Map<Item, Double>  currPrices = discount.calculatePrice(itemsPrices, itemCount, code);
            double price = 0;
            for(Item item: currPrices.keySet()){
                price += currPrices.get(item) * itemCount.get(item);
            }
            if(price > maxPrice){
                maxPrice = price;
                maxDiscount = discount;
            }
        }
        return maxDiscount.getPercent(itemsPrices, itemCount, code);
    }

    private Map<Item, Double> addGetPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception {
        Map<Item, Double> itemsPercent = new HashMap<>();
        for(IDiscount discount : discounts){
            Map<Item, Double> curr = discount.getPercent(itemsPrices, itemCount, code);
            if(itemsPercent.isEmpty())
                itemsPercent = curr;
            else{
                for(Item item : itemsPercent.keySet()){
                    itemsPercent.replace(item, curr.get(item) + itemsPercent.get(item));
                }
            }
        }
        return itemsPercent;
    }
        @Override
    public Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception{
        return switch (numericRule) {
            case MAX -> maxCalculatePrice(itemsPrices, itemsCount, code);
            case ADD -> addCalculatePrice(itemsPrices, itemsCount, code);
            default -> null;
        };
    }

    private Map<Item, Double> maxCalculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception{
        Map<Item, Double>  newPrices = new HashMap<>();
        double maxPrice = 0;
        for(IDiscount discount: discounts){
            Map<Item, Double>  currPrices = discount.calculatePrice(itemsPrices, itemsCount, code);
            double price = 0;
            for(Item item: currPrices.keySet()){
                price += currPrices.get(item) * itemsCount.get(item);
            }
            if(price > maxPrice){
                maxPrice = price;
                newPrices = currPrices;
            }
        }
        return newPrices;

    }

    private Map<Item, Double> addCalculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception{
        Map<Item, Double>  newPrices = new HashMap<>();
        Map<Item, Double> itemsPercent = new HashMap<>();
        for(IDiscount discount : discounts){
            Map<Item, Double> curr = discount.getPercent(itemsPrices, itemsCount, code);
            if(itemsPercent.isEmpty())
                itemsPercent = curr;
            else{
                for(Item item : itemsPercent.keySet()){
                    itemsPercent.replace(item, curr.get(item) + itemsPercent.get(item));
                }
            }
        }
        for(Item item : itemsPrices.keySet()){
            newPrices.put(item, itemsPrices.get(item) * (1-(itemsPercent.get(item))/100));
        }
        return newPrices;

    }


}
