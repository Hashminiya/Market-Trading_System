package DomainLayer.Repositories;

import DAL.BasketItem;
import DAL.BasketItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
@Scope("prototype")
public interface BasketItemRepository extends JpaRepository<BasketItem, BasketItemId> {
}
