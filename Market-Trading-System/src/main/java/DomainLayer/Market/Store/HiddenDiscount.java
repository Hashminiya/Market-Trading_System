package DomainLayer.Market.Store;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;

public class HiddenDiscount extends Discount{

    private String code;

    public HiddenDiscount(Long id, double percent, Date expirationDate, long storeId, String code){
        super(id, percent, expirationDate, storeId);
        this.code = code;
    }

    @Override
    public double calculatePrice(double originalPrice, String code) throws Exception{
        if(!isValid(null))
            throw new CertificateExpiredException("The Discount is has expired");
        if(!this.code.equals(code))
            throw new Exception("Invalid code for discount");
        return originalPrice * (1 - this.percent);
    }
}
