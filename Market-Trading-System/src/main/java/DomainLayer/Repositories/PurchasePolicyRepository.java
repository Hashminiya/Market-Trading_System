package DomainLayer.Repositories;

import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasePolicyRepository extends JpaRepository<PurchasePolicy, Long> {
}
