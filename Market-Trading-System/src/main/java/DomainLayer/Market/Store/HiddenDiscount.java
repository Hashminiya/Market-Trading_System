package DomainLayer.Market.Store;

import java.util.Date;
import java.util.List;

public class HiddenDiscount extends Discount{

    private String code;

    public HiddenDiscount(Long id, double percent, Date expirationDate, List<Long> items, long storeId, String code){
        super(id, percent, expirationDate, items, storeId);
        this.code = code;
    }
}
