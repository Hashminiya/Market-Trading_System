package DomainLayer.Market.Store;

import java.util.Date;
import java.util.List;

public abstract class DiscountComposite implements IDiscount{

    protected Long id;
    protected double percent;
    protected Date expirationDate;
    protected Long storeId;
    protected List<IDiscount> discounts;

    public DiscountComposite(Long id, double percent, Date expirationDate, long storeId, List<IDiscount> discounts){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.storeId = storeId;
        this.discounts = discounts;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }


    @Override
    public Date getExpirationDate(){
        return expirationDate;
    }

    @Override
    public boolean isValid(List<Long> items) {
        //TODO
        return false;
    }

    public abstract double calculatePrice(double originalPrice, String code) throws Exception;

}
