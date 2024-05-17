package DomainLayer.Market.Purchase;

import java.util.Date;
import java.util.List;

public class PurchaseController implements IPurchaseFacade{
    private List<Purchase> purchaseList;
    private boolean isPaymentServiceConnected;
    private boolean isSupplyServiceConnected;
    public PurchaseController(){

    }

    @Override
    public boolean initServices() {
        return false;
    }

    @Override
    public boolean isValidServices() {
        return false;
    }

    @Override
    public boolean checkout(int userID, int creditCard, Date expiryDate, int cvv, List<ItemDTO> purchaseItemsList) {
        return false;
    }

    @Override
    public List<Purchase> getPurchaseByStore(int storeId) {
        return null;
    }

    @Override
    public List<Purchase> getPurchaseByUser(int UserId) {
        return null;
    }
}
