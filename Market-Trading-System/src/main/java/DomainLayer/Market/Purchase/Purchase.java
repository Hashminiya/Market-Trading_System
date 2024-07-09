package DomainLayer.Market.Purchase;

import API.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.Abstractions.IPaymentService;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Repositories.ItemDTORepository;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
public class Purchase implements IPurchase, DataItem<Long> {

    @Transient
    private PaymentServiceProxy paymentServiceProxy;
    @Transient
    private SupplyServiceProxy supplyServiceProxy;

    @Id
    @Getter
    private long purchaseId;

    @ManyToMany
    @JoinTable(
            name = "purchase_items",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )

    private List<ItemDTO> purchasedItemsList;

    private double totalAmount;

    @Getter
    private String userId;

    @Transient
    private ItemDTORepository itemsRepo;

    String purchaseDate;


    public Purchase(String userId,double totalAmount,Long purchaseId,List<ItemDTO> purchasedItemsList, PaymentServiceProxy paymentService, SupplyServiceProxy supplyService) {
            paymentServiceProxy = paymentService;
            supplyServiceProxy = supplyService;
            this.purchaseId=purchaseId;
            this.purchasedItemsList=purchasedItemsList;
            this.totalAmount = totalAmount;
            this.userId = userId;
            itemsRepo = SpringContext.getBean(ItemDTORepository.class);
            for (ItemDTO item:purchasedItemsList) {
                itemsRepo.save(item);
            }
            purchaseDate =LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

    public Purchase() {}

    @PostLoad
    private void initFields() {
        //init state
        this.itemsRepo = SpringContext.getBean(ItemDTORepository.class);
        this.paymentServiceProxy = SpringContext.getBean(PaymentServiceProxy.class);
        this.supplyServiceProxy = SpringContext.getBean(SupplyServiceProxy.class);
        //load shopping cart
    }

    @Override
    public void checkout(String creditCard, Date expiryDate, String CVV) throws Exception {
        int paymentResult = paymentServiceProxy.chargeCreditCard(creditCard,expiryDate,CVV,totalAmount);
        int supplyResult = -1;
        if(paymentResult != -1)
            supplyResult = supplyServiceProxy.performCartSupply();
        int cancelPaymentResult = -1;
        if(supplyResult == -1 && paymentResult != -1) {
            cancelPaymentResult = paymentServiceProxy.cancelPayment(supplyResult);
            while(cancelPaymentResult == -1)
                cancelPaymentResult = paymentServiceProxy.cancelPayment(supplyResult);
        }
        if(paymentResult == -1 || supplyResult == -1)
            throw new RuntimeException("Checkout Failed");
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



}

