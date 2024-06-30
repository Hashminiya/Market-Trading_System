package DomainLayer.Repositories;

import DAL.ItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ItemDTORepository extends JpaRepository<ItemDTO, Long> {
}
