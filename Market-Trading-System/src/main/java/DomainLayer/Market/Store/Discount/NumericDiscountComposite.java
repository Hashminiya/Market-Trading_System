package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.NumericRule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumericDiscountComposite extends DiscountComposite{

    private NumericRule numericRule;

    public NumericDiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, List<IDiscount> discounts, String rule) {
        super(id, percent, expirationDate, storeId, items, categories, discounts);
        this.numericRule = NumericRule.valueOf(rule);
    }


    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        switch (numericRule){
            case MAX:
            //TODO
        }
        return null;
    }

    private Map<Long, Double> maxCalculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        Map<Long, Double>  newPrices = new HashMap<>();
        //TODO
        return null;

    }

    private Map<Long, Double> addCalculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        Map<Long, Double>  newPrices = new HashMap<>();
        //TODO
        return null;

    }
}
