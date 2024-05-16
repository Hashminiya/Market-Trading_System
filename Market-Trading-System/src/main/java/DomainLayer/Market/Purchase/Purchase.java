package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

import java.util.Date;
import java.util.List;

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
    public boolean checkout(List<ItemDTO> itemList, String creditCard, Date expiryDate, String CVV) {
        Boolean isValidCard = paymentServiceProxy.validateCreditCard(creditCard,expiryDate,CVV);
        Boolean canBeSupplied = supplyServiceProxy.validateCartSupply(itemList);
        if(isValidCard & canBeSupplied){
            double amount = calculateAmount(itemList);
            paymentServiceProxy.chargeCreditCard(creditCard,expiryDate,CVV,amount);
            supplyServiceProxy.supplyCart(itemList);
            return true;
        }
        return false;
    }

    public double calculateAmount(List<ItemDTO> itemList) {
        double sum=0;
        for (ItemDTO item:itemList) {
            sum+=item.getTotalPrice();
        }
        return sum;
    }
}

