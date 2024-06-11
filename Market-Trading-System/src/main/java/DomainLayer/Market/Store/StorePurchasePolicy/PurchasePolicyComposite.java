package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.LogicalRule;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class PurchasePolicyComposite extends PurchsePoilcy{
    Map<LogicalRule, BiPredicate<Boolean, Boolean>> logicalOperators = new HashMap<>() {{
        put(LogicalRule.AND, (a, b) -> a && b);
        put(LogicalRule.OR, (a, b) -> a || b);
        put(LogicalRule.XOR, (a, b) -> a ^ b);
    }};
    Map<LogicalRule, Boolean> initValues = new HashMap<>(){{
        put(LogicalRule.AND, true);
        put(LogicalRule.OR, false);
        put(LogicalRule.XOR, false);
    }};
    IRepository<Long, PurchsePoilcy> policies;
    private final LogicalRule logicalRule;
    public PurchasePolicyComposite(Long id, String name,
                                   IRepository<Long, PurchsePoilcy> policies,
                                   LogicalRule logicalRule) {
        super(id, name, null, null, true);
        this.policies = policies;
        this.logicalRule = logicalRule;
    }
    @Override
    public boolean isValid(HashMap<Item, Integer> itemsInBasket, String userDetails) {
        boolean answer = initValues.get(logicalRule);
        for (PurchsePoilcy policy: policies.findAll()
             ) {
            answer = logicalOperators.get(logicalRule).test(answer,policy.isValid(
                    itemsInBasket, userDetails
            ));
        }
        return answer;
    }
}
