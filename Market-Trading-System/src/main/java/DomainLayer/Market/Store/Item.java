package DomainLayer.Market.Store;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Util.DataItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Item implements DataItem<Long> {

    @Id
    private Long id;
    private String name;
    private String description;
    private int quantity;
    private double price;

    @Transient
    private List<String> categories;

    private static ReentrantLock lock = new ReentrantLock();
    private long storeId;

    public Item(Long id, String name,String description, List<String> categories, long storeId){
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.storeId = storeId;
    }

    public Item() {

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
