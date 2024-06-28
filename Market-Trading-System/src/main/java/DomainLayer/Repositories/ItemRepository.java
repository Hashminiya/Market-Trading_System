package DomainLayer.Repositories;

import DomainLayer.Market.Store.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    //List<Item> findByName(String name);
    //List<Item> findByPriceGreaterThan(double price);
    //List<Item> findByNameContaining(String keyword);
    //List<Item> findByCategoriesIn(List<String> categories);
    //List<String> findAllCategory();
    //TODO:: enter Category to table of item
    List<Item> findByStoreIdAndNameContaining(long storeId, String name);

}
