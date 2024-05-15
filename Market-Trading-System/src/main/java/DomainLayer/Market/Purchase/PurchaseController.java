package DomainLayer.Market.Purchase;

import DAL.ItemDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseController implements IPurchaseFacade{
    private List<Purchase> purchaseList;
    private boolean isPaymentServiceConnected;
    private boolean isSupplyServiceConnected;

    public PurchaseController(){
        isPaymentServiceConnected =false;
        isSupplyServiceConnected=false;
        purchaseList = new ArrayList<>();
    }

    @Override
    public boolean initServices() {
        return false;
    }

    @Override
    public boolean isValidServices() {
        return isPaymentServiceConnected & isSupplyServiceConnected;
    }

    @Override
    public boolean checkout(int userID, int creditCard, Date expiryDate, int cvv, List<ItemDTO> purchaseItemsList) {
        //TODO: IMPLEMENT
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
