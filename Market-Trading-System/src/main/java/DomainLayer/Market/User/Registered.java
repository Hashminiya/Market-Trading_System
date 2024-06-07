package DomainLayer.Market.User;

import DomainLayer.Market.Util.StorePermission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("registered")
public class Registered implements Istate{

    public Registered() {
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
        return true;
    }
}
