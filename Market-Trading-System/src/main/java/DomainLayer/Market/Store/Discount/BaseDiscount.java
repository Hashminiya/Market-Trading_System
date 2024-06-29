package DomainLayer.Market.Store.Discount;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Cacheable
public abstract class BaseDiscount implements IDiscount{
    @Id
    protected Long id;
}
