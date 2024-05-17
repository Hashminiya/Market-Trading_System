package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;

public interface IProduct extends DataItem {
    public void addProduct(IProduct newProduct);
    public void setDiscount(Discount discount);
}
