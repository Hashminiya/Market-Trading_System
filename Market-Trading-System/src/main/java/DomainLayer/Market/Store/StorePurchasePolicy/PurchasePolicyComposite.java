package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.LogicalRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

@Entity
@DiscriminatorValue("COMPOSITE")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class PurchasePolicyComposite extends PurchasePolicy {
    @Transient
    Map<LogicalRule, BiPredicate<Boolean, Boolean>> logicalOperators = new HashMap<>() {{
        put(LogicalRule.AND, (a, b) -> a && b);
        put(LogicalRule.OR, (a, b) -> a || b);
        put(LogicalRule.XOR, (a, b) -> a ^ b);
    }};
    @Transient
    Map<LogicalRule, Boolean> initValues = new HashMap<>(){{
        put(LogicalRule.AND, true);
        put(LogicalRule.OR, false);
        put(LogicalRule.XOR, false);
    }};
    @Transient
    List<PurchasePolicy> policies;
    @Transient
    private LogicalRule logicalRule;

    @JsonCreator
    public PurchasePolicyComposite(@JsonProperty("id") Long id, @JsonProperty("name") String name,
                @JsonProperty("policies") List<PurchasePolicy> policies,
                @JsonProperty("logicalRole") LogicalRule logicalRule) {
        super(id, name, null, null, true);
        this.policies = policies;
        this.logicalRule = logicalRule;
    }

    public PurchasePolicyComposite() {

    }

    @Override
    public boolean isValid(HashMap<Item, Integer> itemsInBasket, String userDetails) {
        boolean answer = initValues.get(logicalRule);
        for (PurchasePolicy policy: policies
             ) {
            answer = logicalOperators.get(logicalRule).test(answer,policy.isValid(
                    itemsInBasket, userDetails
            ));
        }
        return answer;
    }

    public List<PurchasePolicy> getPolicies() {
        return policies;
    }
}
