package Market.Purchase;

public class PurchaseController implements IPurchaseFacade{
    private static PurchaseController instance;

    private PurchaseController() {}

    public PurchaseController getInstance() {
        if (instance == null) {
            instance = new PurchaseController();
        }
        return instance;
    }

}
