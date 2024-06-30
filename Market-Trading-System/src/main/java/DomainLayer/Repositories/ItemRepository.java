package DomainLayer.Repositories;

import DomainLayer.Market.Store.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    //List<Item> findByName(String name);
    //List<Item> findByPriceGreaterThan(double price);
    //List<Item> findByNameContaining(String keyword);
    //List<Item> findByCategoriesIn(List<String> categories);
    @Query("SELECT i.categories FROM Item i WHERE i.id = :itemId")
    List<String> findCategoriesByItemId(@Param("itemId") Long itemId);

    @Query("SELECT DISTINCT c FROM Item i JOIN i.categories c WHERE i.storeId = :storeId")
    List<String> findAllCategoriesByStoreId(@Param("storeId") long storeId);

    List<Item> findByStoreIdAndNameContaining(long storeId, String name);

}
