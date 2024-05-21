package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;
import java.util.Date;
import java.util.List;

public abstract class Discount implements DataItem<Long> {

    protected Long id;
    protected double percent;
    protected Date expirationDate;
    protected Long storeId;

    public Discount(Long id, double percent, Date expirationDate, long storeId){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }


    public Date getExpirationDate(){
        return expirationDate;
    }

    public boolean isValid(List<Long> items){
        Date now = new Date();
        return getExpirationDate().after(now);
    }

    public abstract double calculatePrice(double originalPrice, String code) throws Exception;

}
