package DomainLayer.Market.Store.Discount;

import java.util.Map;

public class Condition implements ICondition{

    //private Map<Long, Integer> conditionItem;
    //private double minCost;
    private Long itemId;
    private int count;

    public Condition(Long itemId, int count){
        this.itemId = itemId;
        this.count = count;
    }

    public boolean isValid(Map<Long, Integer> items){
        if(!(items.containsKey(itemId) && items.get(itemId) >= count))
            return false;
        return true;
    }

}
