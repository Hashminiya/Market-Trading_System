package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.DataItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface ICondition extends DataItem<Long> {

    public boolean isValid(Map<Long, Integer> items);


}
