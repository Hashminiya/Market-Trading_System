package DomainLayer.Market.Store;

import java.util.Date;
import java.util.List;

public class RegularDiscount extends Discount{

    private List<Long> conditionItems;

    public RegularDiscount(Long id, double percent, Date expirationDate, long storeId, List<Long> conditionItems){
        super(id, percent, expirationDate, storeId);
        this.conditionItems = conditionItems;
    }

    @Override
    public boolean isValid(List<Long> items){
        Date now = new Date();
        if(!getExpirationDate().after(now))
            return false;
        return (items.containsAll(conditionItems));
    }


    @Override
    public double calculatePrice(double originalPrice, String code) {
        return originalPrice * (1 - this.percent);
    }
}
