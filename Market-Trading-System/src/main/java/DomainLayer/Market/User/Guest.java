package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;

import java.util.List;

public class Guest implements Istate{

    public Guest() {
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

    @Override
    public boolean isRegistered() {
        return false;
    }
}