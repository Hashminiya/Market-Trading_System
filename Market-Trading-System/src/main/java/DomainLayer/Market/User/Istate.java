package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;

import java.util.List;

public interface Istate {
    public boolean checkPermission(String permission, Long storeId);
    public boolean checkPermission(String permission);
    public List<StorePermission> getPermission();
}
