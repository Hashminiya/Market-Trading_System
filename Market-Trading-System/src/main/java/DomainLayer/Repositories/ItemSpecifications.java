package DomainLayer.Repositories;

import DomainLayer.Market.Store.Item;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ItemSpecifications {
    public static Specification<Item> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Item> hasPriceGreaterThan(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), price);
    }

    public static Specification<Item> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }

    public static Specification<Item> categoriesIn(List<String> categories) {
        return (root, query, criteriaBuilder) -> root.join("categories").in(categories);
    }

    /*
    @Service
    public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findItems(String name, double price, String keyword, List<String> categories) {
        return itemRepository.findAll(Specification.where(ItemSpecifications.hasName(name))
                .and(ItemSpecifications.hasPriceGreaterThan(price))
                .and(ItemSpecifications.nameContains(keyword))
                .and(ItemSpecifications.categoriesIn(categories)));
    }
     */
}
