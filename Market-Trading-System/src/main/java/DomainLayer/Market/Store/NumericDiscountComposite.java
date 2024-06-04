package DomainLayer.Market.Store;

import DomainLayer.Market.Util.NumericRule;

import java.util.Date;
import java.util.List;

public class NumericDiscountComposite extends DiscountComposite{

    private NumericRule numericRule;

    public NumericDiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<IDiscount> discounts, String rule) {
        super(id, percent, expirationDate, storeId, discounts);
        this.numericRule = NumericRule.valueOf(rule);
    }

    @Override
    public double calculatePrice(double originalPrice, String code) throws Exception {
        return 0;
    }
}
