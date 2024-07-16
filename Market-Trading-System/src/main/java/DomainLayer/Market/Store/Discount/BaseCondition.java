package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "condition_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class BaseCondition implements ICondition{
    @Id
    protected Long id;
    private String name;

    public BaseCondition(Long id, String name) {
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