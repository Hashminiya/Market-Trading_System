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

    @Query("SELECT i FROM Item i WHERE i.name LIKE %:name%")
    List<Item> findByNameContaining(@Param("name") String name);
    //List<Item> findByPriceGreaterThan(double price);
    //List<Item> findByName(String keyword);

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c = :category")
    List<Item> findByCategory(@Param("category") String category);

    //List<Item> findByCategoriesIn(List<String> categories);
    @Query("SELECT i.categories FROM Item i WHERE i.id = :itemId")
    List<String> findCategoriesByItemId(@Param("itemId") Long itemId);

    @Query("SELECT DISTINCT c FROM Item i JOIN i.categories c WHERE i.storeId = :storeId")
    List<String> findAllCategoriesByStoreId(@Param("storeId") long storeId);

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE i.name LIKE %:keyWord% AND c = :category")
    List<Item> findByNameContainingAndCategory(@Param("keyWord") String keyWord, @Param("category") String category);

    List<Item> findByStoreIdAndNameContaining(long storeId, String name);

    @Query("SELECT i FROM Item i WHERE i.storeId = :storeId")
    List<Item> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT DISTINCT i FROM Item i JOIN i.categories c WHERE c IN :categories AND i.name LIKE %:keyWord%")
    List<Item> findByNameContainingAndCategories(List<String> categories, String keyWord);

    @Query("SELECT DISTINCT i FROM Item i JOIN i.categories c WHERE c IN :categories")
    List<Item> findByCategories(@Param("categories") List<String> categories);

}
