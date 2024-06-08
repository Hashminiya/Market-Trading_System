package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.HashMap;

public class PurchasePolicyComposite extends PurchsePoilcy{
    IRepository<Long, PurchsePoilcy> policies;
    ///TODO add logical connection using ENUM
    public PurchasePolicyComposite(Long id, String name,
                                   IRepository<Long, PurchsePoilcy> policies) {
        super(id, name);
        this.policies = policies;
    }

    @Override
    public boolean isValid(HashMap<Long,Integer> itemsInBasket, String userDetails){
        boolean answer = false;
        for (PurchsePoilcy policy: policies.findAll()) {
            policy.isValid(itemsInBasket, userDetails);
        }
        return  answer;
        ///TODO connect between policies using the logical connection
    }

    public void addPolicy(PurchsePoilcy newPolicy) {
        policies.save(newPolicy);
    }
}
