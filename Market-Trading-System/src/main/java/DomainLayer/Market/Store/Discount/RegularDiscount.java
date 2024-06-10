package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class RegularDiscount extends Discount {

    private ICondition conditions;


    @JsonCreator
    public RegularDiscount(@JsonProperty("id") Long id,
                           @JsonProperty("percent") double percent,
                           @JsonProperty("expirationDate") Date expirationDate,
                           @JsonProperty("storeId") long storeId,
                           @JsonProperty("items") List<Long> items,
                           @JsonProperty("categories") List<String> categories,
                           @JsonProperty("isStore") boolean isStore,
                           @JsonProperty("conditions") ICondition conditionItems){
        super(id, percent, expirationDate, storeId, items, categories, isStore);
        this.conditions = conditionItems;
    }

    @Override
    public boolean isValid(Map<Long, Integer> items, String code){
        Date now = new Date();
        if(!getExpirationDate().after(now))
            return false;
        return conditions.isValid(items);
    }

}
