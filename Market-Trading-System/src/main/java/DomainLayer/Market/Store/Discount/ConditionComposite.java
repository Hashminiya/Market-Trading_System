package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.LogicalRule;

import java.util.List;
import java.util.Map;

public class ConditionComposite implements ICondition{

    private List<ICondition> conditions;
    private LogicalRule rule;

    public ConditionComposite(List<ICondition> conditions, LogicalRule rule){
        this.conditions = conditions;
        this.rule = rule;
    }

    public boolean isValid(Map<Long, Integer> items) {
        boolean result = false;
        for(ICondition condition: conditions){
            switch (rule){
                case OR:
                    if(condition.isValid(items))
                        return true;
                    break;
                case AND:
                    if(!condition.isValid(items))
                        return false;
                    result = true;
                    break;
                case XOR:
                    result ^= condition.isValid(items);
            }
        }
        return result;
    }
}
