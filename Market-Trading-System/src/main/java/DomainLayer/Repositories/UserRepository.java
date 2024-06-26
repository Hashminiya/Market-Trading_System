package DomainLayer.Repositories;

import DomainLayer.Market.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User, String> {
}
