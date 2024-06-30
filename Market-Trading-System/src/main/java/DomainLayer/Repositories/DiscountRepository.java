package DomainLayer.Repositories;

import DomainLayer.Market.Store.Discount.BaseDiscount;
import DomainLayer.Market.Store.Discount.IDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DiscountRepository extends JpaRepository<BaseDiscount, Long> {

}
