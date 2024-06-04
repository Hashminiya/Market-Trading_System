package DomainLayer.Market.Store;

import DomainLayer.Market.Util.LogicalRule;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class LogicalDiscountComposite extends DiscountComposite{

    private LogicalRule logicalRule;

    public LogicalDiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<IDiscount> discounts, String rule) {
        super(id, percent, expirationDate, storeId, discounts);
        this.logicalRule = LogicalRule.valueOf(rule);
    }

    @Override
    public double calculatePrice(double originalPrice, String code) throws Exception {
        return 0;
    }
}
