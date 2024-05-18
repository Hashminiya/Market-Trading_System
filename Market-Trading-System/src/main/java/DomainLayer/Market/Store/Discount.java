package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;

public class Discount implements DataItem<Long> {
    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getName() {
        return "";
    }
}
