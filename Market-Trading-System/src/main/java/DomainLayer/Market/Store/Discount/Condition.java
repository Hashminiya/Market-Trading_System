package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Condition")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class Condition extends BaseCondition{
    //private Map<Long, Integer> conditionItem;
    //private double minCost;
    private Long itemId;
    private int count;

    @JsonCreator
    public Condition(@JsonProperty("itemId") Long itemId,
                     @JsonProperty("count") int count){
        this.itemId = itemId;
        this.count = count;
    }

    public boolean isValid(Map<Long, Integer> items){
        if(!(items.containsKey(itemId) && items.get(itemId) >= count))
            return false;
        return true;
    }

}
