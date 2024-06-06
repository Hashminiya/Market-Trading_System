package DomainLayer.Market.Store.Discount;

import java.util.Map;

public interface ICondition {

    public boolean isValid(Map<Long, Integer> items);


}
