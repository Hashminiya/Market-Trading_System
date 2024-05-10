package Market.Purchase;

import jdk.jshell.spi.ExecutionControl;

public class Purchase implements IPurchase{
        private PaymentManagement paymentManagement;
        private SupplyManagement supplyManagement;
        private Receipt receipt;

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
