package Market.Purchase;

public class PurchaseController {
    private static PurchaseController instance;

    private PurchaseController() {}

    public static PurchaseController getInstance() {
        if (instance == null) {
            instance = new PurchaseController();
        }
        return instance;
    }
}
