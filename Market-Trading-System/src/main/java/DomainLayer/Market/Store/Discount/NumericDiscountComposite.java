package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.NumericRule;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class NumericDiscountComposite extends DiscountComposite{

    private NumericRule numericRule;

    public NumericDiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, List<IDiscount> discounts, String rule) {
        super(id, percent, expirationDate, storeId, items, categories, discounts);
        this.numericRule = NumericRule.valueOf(rule);
    }


    @Override
    public boolean isValid(Map<Long, Integer> items, String code) {
        return false;
    }

    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices) {
        return Map.of();
    }
}
