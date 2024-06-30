package DomainLayer.Repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
@Scope("prototype")
public interface DbItemDTORepository extends ItemDTORepository{
}
