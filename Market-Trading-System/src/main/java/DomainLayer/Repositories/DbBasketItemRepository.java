package DomainLayer.Repositories;


import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("db")
@Scope("prototype")
public interface DbBasketItemRepository extends BasketItemRepository {
}
