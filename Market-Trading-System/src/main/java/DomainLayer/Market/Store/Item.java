package DomainLayer.Market.Store;


import DomainLayer.Market.DataItem;
import DomainLayer.Market.IRepository;

public class Item implements IProduct {
    private final Long id;
    private String name;
    private int quantity;
    private double price;
    private final IRepository<Long,Discount> discounts;

    public Item(Long id, String name, IRepository<Long, Discount> discounts){
        this.id = id;
        this.name = name;
        this.discounts = discounts;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addProduct(IProduct newProduct, Long categoryId) {
        /*
        This Function Not suppose to be activated,
        implemented here for interface reasons.
         */
        throw new RuntimeException(String.format("Adding product '%s' failed", newProduct.getName()));
    }

    public void setDiscount(Discount discount){
        this.discounts.save(discount);
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public  void setPrice(double price){
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public double getCurrentPrice(){
        double price = this.price;
        for (Discount discount :discounts.findAll()){
            ///TODO decide if discount nee to be activated and calculate new price accordingly
        }
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
}
