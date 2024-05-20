package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;

import java.util.List;

public class StoreOwner implements Istate {

    public StoreOwner() {
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
