package DomainLayer.Market.Store;

import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Util.DataItem;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements DataItem<Long> {

    @Id
    private Long id;
    private String name;
    private String description;
    private int quantity;
    private double price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    @BatchSize(size = 25)
    private List<String> categories;

    private static ReentrantLock lock = new ReentrantLock();
    @Column(name = "store_id")
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
            unlock();
            throw new RuntimeException(String.format("failed to update %s amount",name));
        }
        else {
            this.quantity -= toDecrease;
            if(this.quantity == 0){
                //TODO: should delete from repo?
            }
        }
        unlock();
    }

    public void increase(int toIncrease) throws InterruptedException{
        lock();
        this.quantity += toIncrease;
        unlock();
    }

    public void lock() throws InterruptedException{
        lock.tryLock(10, TimeUnit.SECONDS);
    }

    public void unlock(){
        lock.unlock();
    }

    public long getStoreId() {
        return storeId;
    }
}
