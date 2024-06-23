package DomainLayer.Repositories;

import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Util.IRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DiscountRepository extends JpaRepository<IDiscount, Long> {
}
