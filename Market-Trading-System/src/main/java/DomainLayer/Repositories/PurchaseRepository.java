package DomainLayer.Repositories;

import DomainLayer.Market.Purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
