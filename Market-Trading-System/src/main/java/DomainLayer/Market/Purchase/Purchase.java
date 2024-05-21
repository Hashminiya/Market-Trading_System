package DomainLayer.Market.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase implements IPurchase, DataItem<Long> {

        private PaymentServiceProxy paymentServiceProxy;
        private SupplyServiceProxy supplyServiceProxy;

        private long purchaseId;
        private List<ItemDTO> purchasedItemsList;
        private double totalAmount;
        private String userId;
        private IRepository<Long,ItemDTO> itemsRepo;
        String purchaseDate;


    public Purchase(String userId,double totalAmount,Long purchaseId,List<ItemDTO> purchasedItemsList, PaymentServiceProxy paymentService, SupplyServiceProxy supplyService) {
            paymentServiceProxy = paymentService;
            supplyServiceProxy = supplyService;
            this.purchaseId=purchaseId;
            this.purchasedItemsList=purchasedItemsList;
            this.totalAmount = totalAmount;
            this.userId = userId;
            itemsRepo = new InMemoryRepository<>();
            for (ItemDTO item:purchasedItemsList) {
                itemsRepo.save(item);
            }
            purchaseDate =LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

    @Override
    public void checkout(String creditCard, Date expiryDate, String CVV) {
        Boolean isValidCard = paymentServiceProxy.validateCreditCard(creditCard,expiryDate,CVV,totalAmount);
        Boolean canBeSupplied = supplyServiceProxy.validateCartSupply(purchasedItemsList);
        if(isValidCard & canBeSupplied){
            paymentServiceProxy.chargeCreditCard(creditCard,expiryDate,CVV,totalAmount);
            supplyServiceProxy.performCartSupply(purchasedItemsList);
        }
        else if(!isValidCard)
            throw new RuntimeException("Checkout Failed\nTransaction cannot be made with that credit card");
        else if(!canBeSupplied)
            throw new RuntimeException("Checkout Failed\nOne of the items can not be supplied");
    }

    public List<ItemDTO> getItemByStore(Long storeId){
       List<ItemDTO> allItems =  itemsRepo.findAll();
        List<ItemDTO> toRet =  new ArrayList<>();
        for (ItemDTO item:allItems) {
            if(storeId.equals(item.getStoreId()))
                toRet.add(item);
        }
        return  toRet;
    }

    @Override
    public Long getId() {
        return purchaseId;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getUserId() {return userId;}

}

