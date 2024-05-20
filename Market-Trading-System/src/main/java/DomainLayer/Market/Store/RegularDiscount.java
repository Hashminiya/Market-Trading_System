package DomainLayer.Market.Store;

import java.util.Date;
import java.util.List;

public class RegularDiscount extends Discount{

    private List<Long> conditionItems;

    public RegularDiscount(Long id, double percent, Date expirationDate, List<Long> items, long storeId, List<Long> conditionItems){
        super(id, percent, expirationDate, items, storeId);
        this.conditionItems = conditionItems;
    }



}
