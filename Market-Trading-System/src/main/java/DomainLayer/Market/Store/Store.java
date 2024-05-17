package DomainLayer.Market.Store;

import DomainLayer.Market.DataItem;
import DomainLayer.Market.Discount;
import DomainLayer.Market.IRepository;
import DomainLayer.Market.InMemoryRepository;
import DomainLayer.Market.Discount;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Store implements DataItem<Long> {
    private final Long id;
    private Long founderId;
    private String name;
    private String description;
    private final List<Long> owners;
    private final List<Long> managers;
    private final InMemoryRepositoryStore products;
    private IRepository<Long , Discount> discounts;
    public Store(Long id, Long founderId, String name, String description,IRepository<Long, Discount> discounts){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.discounts = discounts;
        this.products = new InMemoryRepositoryStore();
        owners = new ArrayList<>();
        managers = new ArrayList<>();
    }

    private long genrateId() {
        return 0;
        UUID id = new UUID().;
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    public List<Long> getOwners(){
        return owners;
    }
    public List<Long> getManagers(){
        return managers;
    }
    public void assignOwner(Long newOwnerId){
        owners.add(newOwnerId);
    }
    public void assignManager(Long newManagerId){
        managers.add(newManagerId);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addItem(String name, double price, int quantity, String description, String[] categories){
        long itemId = genrateId();
        Item newItem = new Item(itemId, name, description ,new InMemoryRepository<>(), categories);
        newItem.setPrice(price);
        newItem.setQuantity(quantity);
        products.save(newItem);

    }

    public void addDiscount(List<Long> items, Discount discount){
        /*A method for adding regular discount, un conditional, assign to an item or a category.*/
        for(Long item : items){
            products.findById(item).setDiscount(discount);
        }
    }

    public void addDiscount(Discount discount){
        /*A method for adding store discount, can be conditional.*/
        discounts.save(discount);
    }
    void updateItem(long itemId, String newName, double newPrice, int quantity){
//        products.update(new Item(itemId));
    }
    void deleteItem(long itemId){
        products.delete(itemId);
    }
    public void changeDiscountPolicy(){
        ///TODO
        throw new UnsupportedOperationException("changeDiscountPolicy method is not implemented yet");
    }
    public List<Item> search(String keyWord){
        return products.search(null ,keyWord,false);
    }
    public List<Item> searchWithCategory(String category,String keyWord){
        return products.search(category ,keyWord,true);
    }
    public List<String> getAllCategories(){
        return products.getAllCategoryValues();
    }
}
