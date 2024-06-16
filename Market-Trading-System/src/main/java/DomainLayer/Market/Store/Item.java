package DomainLayer.Market.Store;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Item implements DataItem<Long> {
    private final Long id;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private List<String> categories;
    private static ReentrantLock lock = new ReentrantLock();

    public Item(Long id, String name,String description, List<String> categories){
        this.id = id;
        this.name = name;
        this.description = description;
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
        return this.price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity)throws InterruptedException{
        lock();
        this.quantity = quantity;
        unlock();
    }

    public  void setPrice(double price)throws InterruptedException{
        lock();
        this.price = price;
        unlock();
    }

    public void setDescription(String description)throws InterruptedException {
        lock();
        this.description = description;
        unlock();
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setName(String newName)throws InterruptedException {
        lock();
        this.name = newName;
        unlock();
    }

    public void decrease(int toDecrease) throws InterruptedException{
        lock();
        if(this.quantity < toDecrease){
            throw new RuntimeException(String.format("failed to update %s amount",name));
        }
        else {this.quantity -= toDecrease;}
        unlock();
    }

    public void lock() throws InterruptedException{
        lock.tryLock(10, TimeUnit.SECONDS);
    }

    public void unlock(){
        lock.unlock();
    }

}
