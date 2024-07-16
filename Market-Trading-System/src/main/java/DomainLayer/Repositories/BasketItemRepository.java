package DomainLayer.Repositories;

import DAL.BasketItem;
import DAL.BasketItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile("db")
@Scope("prototype")
public interface BasketItemRepository extends JpaRepository<BasketItem, BasketItemId> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM basket_items WHERE basket_id = :basketId", nativeQuery = true)
    void deleteByBasketId(@Param("basketId") long basketId);
}
