package DomainLayer.Repositories;

import DAL.BasketItem;
import DAL.BasketItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface BasketItemRepository extends JpaRepository<BasketItem, BasketItemId> {

    @Modifying
    @Query("DELETE FROM BasketItem i WHERE i.id.basketId = :id")
    void deleteByBasket(@Param ("id") Long id);
    //TODO : Maybe need to add Db repo and in-mem repo ?
}
