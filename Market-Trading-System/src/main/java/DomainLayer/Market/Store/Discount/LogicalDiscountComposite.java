package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.LogicalRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LogicalDiscountComposite extends DiscountComposite{

    private LogicalRule logicalRule;

    @JsonCreator
    public LogicalDiscountComposite(@JsonProperty("id") Long id,
                                    @JsonProperty("percent") double percent,
                                    @JsonProperty("expirationDate") Date expirationDate,
                                    @JsonProperty("storeId") long storeId,
                                    @JsonProperty("items") List<Long> items,
                                    @JsonProperty("categories") List<String> categories,
                                    @JsonProperty("isStore") boolean isStore,
                                    @JsonProperty("discounts") List<IDiscount> discounts,
                                    @JsonProperty("logicalRule") String rule) {
        super(id, percent, expirationDate, storeId, items, categories, isStore, discounts);
        this.logicalRule = LogicalRule.valueOf(rule);
    }


    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code){
        switch (logicalRule){
            case OR:
                return orCalculatePrice(itemsPrices, itemsCount, code);
            case AND:
                return andCalculatePrice(itemsPrices, itemsCount, code);
            case XOR:
                return xorCalculatePrice(itemsPrices, itemsCount, code);
            default:
                return null;
        }
    }


    private Map<Long, Double> orCalculatePrice(Map<Long, Double> basketItems, Map<Long, Integer> itemsCount, String code){
        Map<Long, Double> newPrices = new HashMap<>();
        for(IDiscount discount: discounts){
            if(discount.isValid(itemsCount, code))
                newPrices = discount.calculatePrice(basketItems, itemsCount, code);
        }
        return newPrices;
    }

    private Map<Long, Double> andCalculatePrice(Map<Long, Double> basketItems, Map<Long, Integer> itemsCount, String code){
        Map<Long, Double> newPrices = new HashMap<>();
        if(isValid(itemsCount, code)){
            for(IDiscount discount: discounts){
                newPrices = discount.calculatePrice(basketItems, itemsCount, code);
            }
        }
        return newPrices;
    }

    private Map<Long, Double> xorCalculatePrice(Map<Long, Double> basketItems, Map<Long, Integer> itemsCount, String code){
        Map<Long, Double> newPrices = new HashMap<>();
        //TODO
        return newPrices;
    }
}
