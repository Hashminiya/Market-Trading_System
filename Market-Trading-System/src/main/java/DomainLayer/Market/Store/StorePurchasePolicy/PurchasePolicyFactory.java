package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.List;

public class PurchasePolicyFactory {

    private final IUserFacade userFacade;
    IRepository<Long, Item> productsRepo;

    public PurchasePolicyFactory(IUserFacade userFacade, IRepository<Long, Item> productsRepo) {
        this.userFacade = userFacade;
        this.productsRepo = productsRepo;
    }

    public void createPolicy(PurchsePoilcy policy) {
        if(policy instanceof  PurchasePolicyComposite){
            for (PurchsePoilcy p: policy.getPolicies()
                 ) {
                createPolicy(p);
            }
        }
        if (policy instanceof AgeRestrictedPurchasePolicy) {
            ((AgeRestrictedPurchasePolicy) policy).setUserFacade(userFacade);
        }
    }
}