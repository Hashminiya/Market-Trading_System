package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class PurchasePolicyFactory {

    private final IUserFacade userFacade;

    public PurchasePolicyFactory(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void createPolicy(PurchasePolicy policy) {
        policy.setId(IdGenerator.generateId());
        if(policy instanceof  PurchasePolicyComposite){
            for (PurchasePolicy p: policy.getPolicies()
                 ) {
                createPolicy(p);
            }
        }
        if (policy instanceof AgeRestrictedPurchasePolicy) {
            ((AgeRestrictedPurchasePolicy) policy).setUserFacade(userFacade);
        }
    }
}