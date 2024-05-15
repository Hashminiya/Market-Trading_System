package DomainLayer.Market.Purchase;

import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

public class Purchase implements IPurchase{
        private PaymentServiceProxy paymentServiceProxy;
        private SupplyServiceProxy supplyServiceProxy;
        private Receipt receipt;

        public Purchase(PaymentServiceImpl paymentService,SupplyServiceImpl supplyService) {
            paymentServiceProxy = new PaymentServiceProxy(paymentService);
            supplyServiceProxy = new SupplyServiceProxy(supplyService);
            receipt = new Receipt();
        }

    @Override
    public void checkout() {
        //TODO: implement this
    }
}
