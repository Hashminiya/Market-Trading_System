package DomainLayer.Repositories;

import DomainLayer.Market.Store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface StoreRepository extends JpaRepository<Store, Long> {
}
