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

    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices) {
        return Map.of();
    }


    private Map<Long, Integer> orCalculatePrice(Map<Long, Double> basketItems, String code){
        Map<Long, Integer> newPrices = new HashMap<>();

        return newPrices;
    }
}
