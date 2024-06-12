package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.LogicalRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.yaml.snakeyaml.events.Event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LogicalDiscountComposite extends DiscountComposite{

    private LogicalRule logicalRule;
    private int decision;

    @JsonCreator
    public LogicalDiscountComposite(@JsonProperty("id") Long id,
                                    @JsonProperty("discounts") List<IDiscount> discounts,
                                    @JsonProperty("logicalRule") String rule,
                                    @JsonProperty("decision") int decision) {
        super(id, discounts);
        this.logicalRule = LogicalRule.valueOf(rule);
        this.decision = decision;
    }

    @Override
    public boolean isValid(Map<Item, Integer> items, String code){
        switch (logicalRule){
            case OR:
                for(IDiscount discount: discounts){
                    if(discount.isValid(items, code))
                        return true;
                }
                return false;
            case AND:
                for(IDiscount discount: discounts){
                    if(!discount.isValid(items, code))
                        return false;
                }
                return true;
            case XOR:
                boolean result = discounts.get(0).isValid(items, code);
                for(int i = 1; i < discounts.size(); i++){
                    result = result ^ discounts.get(i).isValid(items, code);
                }
                return result;
            default:
                return false;
        }
    }


    @Override
    public Map<Item, Double> calculatePrice(Map<Item, Double> itemsPrices, Map<Item, Integer> itemsCount, String code) throws Exception{
        if(discounts == null || discounts.isEmpty())
            throw new Exception("Empty discounts list in composite of discounts");
        return switch (logicalRule) {
            case OR -> orCalculatePrice(itemsPrices, itemsCount, code);
            case AND -> andCalculatePrice(itemsPrices, itemsCount, code);
            case XOR -> xorCalculatePrice(itemsPrices, itemsCount, code);
            default -> null;
        };
    }


    private Map<Item, Double> orCalculatePrice(Map<Item, Double> basketItems, Map<Item, Integer> itemsCount, String code) throws Exception{
        for(IDiscount discount: discounts){
            if(discount.isValid(itemsCount, code))
                basketItems = discount.calculatePrice(basketItems, itemsCount, code);
        }
        return basketItems;
    }

    private Map<Item, Double> andCalculatePrice(Map<Item, Double> basketItems, Map<Item, Integer> itemsCount, String code) throws Exception{
        if(isValid(itemsCount, code)){
            for(IDiscount discount: discounts){
                basketItems = discount.calculatePrice(basketItems, itemsCount, code);
            }
        }
        return basketItems;
    }

    private Map<Item, Double> xorCalculatePrice(Map<Item, Double> basketItems, Map<Item, Integer> itemsCount, String code) throws Exception{
        boolean result = discounts.get(0).isValid(itemsCount, code);
        for(int i = 1; i < discounts.size(); i++){
            result = result ^ discounts.get(i).isValid(itemsCount, code);
            if(result && decision == 0)
                basketItems = discounts.get(i-1).calculatePrice(basketItems, itemsCount, code);
            else
                basketItems = discounts.get(i).calculatePrice(basketItems, itemsCount, code);
        }
        return basketItems;
    }

    @Override
    public Map<Item, Double> getPercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception{
        if(discounts == null || discounts.isEmpty())
            throw new Exception("Empty discounts list in composite of discounts");
        return switch (logicalRule) {
            case OR -> orCalculatePercent(itemsPrices, itemCount, code);
            case AND -> andCalculatePercent(itemsPrices, itemCount, code);
            case XOR -> xorCalculatePercent(itemsPrices, itemCount, code);
            default -> null;
        };
    }

    private Map<Item, Double> orCalculatePercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception {
        Map<Item, Double> itemsPercent = discounts.get(0).getPercent(itemsPrices, itemCount, code);
        for(Item item: itemsPercent.keySet()){
            itemsPercent.replace(item, 1 - (itemsPercent.get(item) / 100));
        }
        if(isValid(itemCount, code)) {
            for (int i = 1; i < discounts.size(); i++) {
                IDiscount discount = discounts.get(i);
                Map<Item, Double> curr = discount.getPercent(itemsPrices, itemCount, code);
                for (Item item : itemsPercent.keySet()) {
                    itemsPercent.replace(item, itemsPercent.get(item) * (1 - curr.get(item) / 100));
                }

            }
        }
        for (Item item : itemsPercent.keySet()) {
            itemsPercent.replace(item, (1 - itemsPercent.get(item)) * 100);
        }
        return itemsPercent;
    }

    private Map<Item, Double> andCalculatePercent(Map<Item, Double> itemsPrices, Map<Item, Integer> itemCount, String code) throws Exception {
        Map<Item, Double> itemsPercent = new HashMap<>();
        if(isValid(itemCount, code)) {
            itemsPercent = discounts.get(0).getPercent(itemsPrices, itemCount, code);
            for (Item item : itemsPercent.keySet()) {
                itemsPercent.replace(item, 1 - (itemsPercent.get(item) / 100));
            }
            for (int i = 1; i < discounts.size(); i++) {
                IDiscount discount = discounts.get(i);
                Map<Item, Double> curr = discount.getPercent(itemsPrices, itemCount, code);
                for (Item item : itemsPercent.keySet()) {
                    itemsPercent.replace(item, itemsPercent.get(item) * (1 - curr.get(item) / 100));
                }
            }
        }
        for (Item item : itemsPercent.keySet()) {
            itemsPercent.replace(item, (1 - itemsPercent.get(item)) *100);
        }
        return itemsPercent;
    }

    private Map<Item, Double> xorCalculatePercent(Map<Item, Double> itemsPrices,Map<Item, Integer> itemCount, String code) throws Exception {
        Map<Item, Double> itemsPercent = new HashMap<>();
        for (Item item : itemCount.keySet()) {
            itemsPercent.put(item, 1.0);
        }
        boolean result = discounts.get(0).isValid(itemCount, code);
        for(int i = 0; i < discounts.size(); i++){
            result = result ^ discounts.get(i).isValid(itemCount, code);
            Map<Item, Double> curr = new HashMap<>();
            if(result && decision == 0)
                curr = discounts.get(i - 1).getPercent(itemsPrices, itemCount, code);
            else
                curr = discounts.get(i).getPercent(itemsPrices, itemCount, code);
            for (Item item : itemsPercent.keySet()) {
                itemsPercent.replace(item, itemsPercent.get(item) * (1 - curr.get(item) / 100));
            }
        }
        for (Item item : itemsPercent.keySet()) {
            itemsPercent.replace(item, (1 - itemsPercent.get(item)) * 100);
        }
        return itemsPercent;
    }
}
