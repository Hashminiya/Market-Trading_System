package DomainLayer.Market.Store;

import DomainLayer.Market.DataItem;
import DomainLayer.Market.Discount;

public interface IProduct extends DataItem<Long> {
    public void addProduct(IProduct newProduct);
    public void setDiscount(Discount discount);
}
