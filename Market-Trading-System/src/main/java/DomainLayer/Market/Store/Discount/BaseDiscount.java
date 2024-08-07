package DomainLayer.Market.Store.Discount;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discount_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class BaseDiscount implements IDiscount{
    @Id
    private Long id;
    private String name;

    public BaseDiscount(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}