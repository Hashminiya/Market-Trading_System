package DomainLayer.Repositories;

import DAL.ItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDTORepository extends JpaRepository<ItemDTO, Long> {
}
