package DomainLayer.Repositories;

import DomainLayer.Market.ShoppingBasket;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface BasketRepository extends JpaRepository<ShoppingBasket, Long> {
}
