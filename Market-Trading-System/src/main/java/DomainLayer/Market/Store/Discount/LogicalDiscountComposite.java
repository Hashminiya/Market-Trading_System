package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.LogicalRule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicalDiscountComposite extends DiscountComposite{

    private LogicalRule logicalRule;

    public LogicalDiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, List<IDiscount> discounts, String rule) {
        super(id, percent, expirationDate, storeId, items, categories, discounts);
        this.logicalRule = LogicalRule.valueOf(rule);
    }


    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) throws Exception{
        switch (logicalRule){
            case OR:
                return orCalculatePrice(itemsPrices, itemsCount, code);
            case AND:
                return andCalculatePrice(itemsPrices, itemsCount, code);
            case XOR:
                return xorCalculatePrice(itemsPrices, itemsCount, code);
            default:
                throw new Exception("Logical discount missing logical rule");
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
