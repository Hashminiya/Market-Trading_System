package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.LogicalRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ConditionComposite")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ConditionComposite extends BaseCondition{

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "condition_id")
    private List<BaseCondition> conditions;

    @Enumerated(EnumType.STRING)
    @Column(name = "logical_rule")
    private LogicalRule rule;

    @JsonCreator
    public ConditionComposite(@JsonProperty("conditions") List<BaseCondition> conditions,
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
