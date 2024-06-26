package DomainLayer.Repositories;

import DomainLayer.Market.ShoppingBasket;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BasketRepository extends JpaRepository<ShoppingBasket, Long> {
}
