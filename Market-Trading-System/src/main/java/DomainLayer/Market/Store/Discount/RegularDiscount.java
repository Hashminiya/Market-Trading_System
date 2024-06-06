package DomainLayer.Market.Store.Discount;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegularDiscount extends Discount {

    private ICondition conditions;

    public RegularDiscount(Long id, double percent, Date expirationDate, long storeId, ICondition conditionItems){
        super(id, percent, expirationDate, storeId);
        this.conditions = conditionItems;
    }

    @Override
    public boolean isValid(Map<Long, Integer> items, String code){
        Date now = new Date();
        if(!getExpirationDate().after(now))
            return false;
        return conditions.isValid(items);
    }

}
