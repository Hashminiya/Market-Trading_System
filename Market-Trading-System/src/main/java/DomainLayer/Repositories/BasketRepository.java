package DomainLayer.Repositories;

import DomainLayer.Market.ShoppingBasket;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

@Scope("prototype")
public interface BasketRepository extends JpaRepository<ShoppingBasket, Long> {
}
