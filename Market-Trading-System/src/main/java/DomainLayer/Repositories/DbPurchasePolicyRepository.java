package DomainLayer.Repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
@Profile("db")
public interface DbPurchasePolicyRepository extends PurchasePolicyRepository{
}
