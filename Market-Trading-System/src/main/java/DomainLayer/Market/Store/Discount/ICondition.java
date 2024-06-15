package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface ICondition {

    public boolean isValid(Map<Long, Integer> items);


}
