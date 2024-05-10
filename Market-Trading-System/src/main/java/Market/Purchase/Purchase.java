package Market.Purchase;

import jdk.jshell.spi.ExecutionControl;

import java.util.List;

public class Purchase implements IPurchase{
        private PaymentManagement paymentManagement;
        private SupplyManagement supplyManagement;
        private Receipt receipt;
        private int buyerId;
        private int storeId;
        private List<Integer> itemsIds;


        public Purchase() {
            paymentManagement = new PaymentManagement();
            supplyManagement = new SupplyManagement();
            receipt = new Receipt();
        }

    @Override
    public void purchaseProduct() {
        //TODO: implement this
    }
}
