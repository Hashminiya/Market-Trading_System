package DomainLayer.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("db")
@Scope("prototype")
public interface DbItemRepository extends ItemRepository{

    List<String> findAllCategories();
}
