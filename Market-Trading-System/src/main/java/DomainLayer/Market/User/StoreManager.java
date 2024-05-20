package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;

import java.util.List;

public class StoreManager implements Istate{

    public StoreManager() {
    }

    @Override
    public boolean checkPermission(String permission, Long storeId) {
        return false;
    }

    @Override
    public boolean checkPermission(String permission) {
        return false;
    }

    @Override
    public List<StorePermission> getPermission() {
        return List.of();
    }
}
