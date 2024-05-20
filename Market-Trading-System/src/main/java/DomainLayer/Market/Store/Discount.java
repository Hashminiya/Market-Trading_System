package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;
import java.util.Date;
import java.util.List;

public abstract class Discount implements DataItem<Long> {

    private Long id;
    private double percent;
    private Date expirationDate;
    private List<Long> items;
    private Long storeId;

    public Discount(Long id, double percent, Date expirationDate,List<Long> items, long storeId){
        this.id = id;
        this.percent = percent;
        this.expirationDate = expirationDate;
        this.items = items;
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

    public List<Long> getItems(){
        return items;
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public abstract double getItemPrice(long itemId, double originalPrice);

    public abstract double getItemPrice()

}
