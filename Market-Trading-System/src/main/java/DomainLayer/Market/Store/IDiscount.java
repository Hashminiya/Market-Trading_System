package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;

import java.util.Date;
import java.util.List;

public interface IDiscount extends DataItem<Long> {

    public Long getId();

    public String getName();

    public Date getExpirationDate();

    public boolean isValid(List<Long> items);

    public double calculatePrice(double originalPrice, String code) throws Exception;
}
