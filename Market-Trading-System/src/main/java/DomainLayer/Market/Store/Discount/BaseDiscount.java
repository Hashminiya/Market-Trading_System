package DomainLayer.Market.Store.Discount;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public abstract class BaseDiscount implements IDiscount{
    @Id
    protected Long id;
}
