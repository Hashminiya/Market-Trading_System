package DomainLayer.Market.Store;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;

import java.util.List;

public class Item implements DataItem<Long> {
    private final Long id;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private final IRepository<Long, Discount> discounts;
    private List<String> categories;

    public Item(Long id, String name,String description ,IRepository<Long, Discount> discounts, List<String> categories){
        this.id = id;
        this.name = name;
        this.description = description;
        this.discounts = discounts;
        this.categories = categories;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getCurrentPrice(String code) throws Exception {
        double currentPrice = this.price;
        for (Discount discount :discounts.findAll()){
            if(discount.isValid(null)){
                currentPrice = discount.calculatePrice(currentPrice, code);
            }
        }
        return currentPrice;
    }

    public int getQuantity() {
        return quantity;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void decrease(int toDecrease) {
        if(this.quantity < toDecrease){
            throw new RuntimeException(String.format("failed to update %s amount",name));
        }
        else {this.quantity -= toDecrease;}
    }

    public List<Discount> getDiscounts() {
        return discounts.findAll();
    }
}
