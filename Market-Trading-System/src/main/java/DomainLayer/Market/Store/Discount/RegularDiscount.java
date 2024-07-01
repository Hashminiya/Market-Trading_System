package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("RegularDiscount")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class RegularDiscount extends Discount {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "condition_id")
    private BaseCondition conditions;


    @JsonCreator
    public RegularDiscount(@JsonProperty("id") Long id,
                           @JsonProperty("percent") double percent,
                           @JsonProperty("expirationDate") Date expirationDate,
                           @JsonProperty("storeId") long storeId,
                           @JsonProperty("items") List<Long> items,
                           @JsonProperty("categories") List<String> categories,
                           @JsonProperty("isStore") boolean isStore,
                           @JsonProperty("conditions") BaseCondition conditionItems){
        super(id, percent, expirationDate, storeId, items, categories, isStore);
        this.conditions = conditionItems;
    }


    @Override
    public boolean isValid(Map<Item, Integer> items, String code){
        Date now = new Date();
        if(!expirationDate.after(now))
            return false;
        Map<Long, Integer> itemsIds = new HashMap<>();
        for(Item item: items.keySet()) {
            itemsIds.put(item.getId(), items.get(item));
        }
        return conditions.isValid(itemsIds);
    }



}
