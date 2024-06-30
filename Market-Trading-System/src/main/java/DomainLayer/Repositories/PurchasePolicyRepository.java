package DomainLayer.Repositories;

import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface PurchasePolicyRepository extends JpaRepository<PurchasePolicy, Long> {
}
