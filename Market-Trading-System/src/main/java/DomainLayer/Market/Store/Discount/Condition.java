package DomainLayer.Market.Store.Discount;

import java.util.Map;

public class Condition implements ICondition{

    private Map<Long, Integer> conditionItem;
    //private double minCost;

    public Condition(Map<Long, Integer> items){
        this.conditionItem = items;
    }

    public boolean isValid(Map<Long, Integer> items){
        for(Long itemId: conditionItem.keySet()){
            if(!(items.containsKey(itemId) && items.get(itemId) >= conditionItem.get(itemId)))
                return false;
        }
        return true;
    }

}
