package DomainLayer.Market.Store;


import DomainLayer.Market.DataItem;
import DomainLayer.Market.IRepository;

public class Item implements IProduct {
    public static class Discount implements DataItem {

        @Override
        public Long getId() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    }
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private IRepository<Discount> discounts;

    public Item(Long id, String name){
        this.id = id;
        this.name = name;
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

    public int getQuantity() {
        return quantity;
    }
}
