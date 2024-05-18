package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;

import java.util.Date;
import java.util.List;

public class Purchase implements IPurchase{
        private PaymentServiceProxy paymentServiceProxy;
        private SupplyServiceProxy supplyServiceProxy;

        public Purchase(PaymentServiceProxy paymentService, SupplyServiceProxy supplyService) {
            paymentServiceProxy = paymentService;
            supplyServiceProxy = supplyService;

        }


    @Override
    public boolean checkout(List<ItemDTO> itemList, String creditCard, Date expiryDate, String CVV) {
        Boolean isValidCard = paymentServiceProxy.validateCreditCard(creditCard,expiryDate,CVV);
        Boolean canBeSupplied = supplyServiceProxy.validateCartSupply(itemList);
        if(isValidCard & canBeSupplied){
            double amount = calculateAmount(itemList);
            paymentServiceProxy.chargeCreditCard(creditCard,expiryDate,CVV,amount);
            supplyServiceProxy.performCartSupply(itemList);
            return true;
        }
        if(!isValidCard)
            throw new RuntimeException("Checkout Failed\nTransaction cannot be made with that credit card");
        if(!canBeSupplied)
            throw new RuntimeException("Checkout Failed\nOne of the items can not be supplied");
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

