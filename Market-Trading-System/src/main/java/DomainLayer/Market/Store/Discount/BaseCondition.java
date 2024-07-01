package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "condition_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class BaseCondition implements ICondition{
    @Id
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }
}
