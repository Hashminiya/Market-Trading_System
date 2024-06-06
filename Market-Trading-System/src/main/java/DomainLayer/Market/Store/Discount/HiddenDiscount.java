package DomainLayer.Market.Store.Discount;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HiddenDiscount extends Discount {

    private String code;

    public HiddenDiscount(Long id, double percent, Date expirationDate, long storeId, List<Long> items, List<String> categories, String code){
        super(id, percent, expirationDate, storeId, items, categories);
        this.code = code;
    }

    @Override
    public boolean isValid(Map<Long, Integer> items, String code){
        Date now = new Date();
        return (getExpirationDate().after(now) && this.code.equals(code));
    }

}
