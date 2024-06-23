package DomainLayer.Repositories;

import DomainLayer.Market.Store.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    List<Item> findByName(String name);
    List<Item> findByPriceGreaterThan(double price);
    List<Item> findByNameContaining(String keyword);
    List<Item> findByCategoriesIn(List<String> categories);
    List<String> findAllCategories();
}
