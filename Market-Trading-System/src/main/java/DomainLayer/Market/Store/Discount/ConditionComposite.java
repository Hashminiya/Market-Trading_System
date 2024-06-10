package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.LogicalRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ConditionComposite implements ICondition{

    private List<ICondition> conditions;
    private LogicalRule rule;

    @JsonCreator
    public ConditionComposite(@JsonProperty("conditions") List<ICondition> conditions,
                              @JsonProperty("rule") LogicalRule rule){
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
